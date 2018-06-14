package com.example.alfonsohernandez.boardgamebestfriends.presentation.places

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlaceActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail.PlaceDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.messaging.RemoteMessage
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_places.*
import java.util.*
import javax.inject.Inject

class PlacesFragment : Fragment(),
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMarkerClickListener,
        PlacesContract.View,
        SwipeRefreshLayout.OnRefreshListener{

    private val TAG = "PlacesFragment"

    lateinit var mMapView: MapView
    private var mMap: GoogleMap? = null

    lateinit var mPlaceDetectionClient: PlaceDetectionClient
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var client: GoogleApiClient

    @Inject
    lateinit var presenter: PlacesPresenter

    lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_places, container, false)

        mMapView = rootView.findViewById(R.id.mapView)

        mMapView.onCreate(savedInstanceState)
        mMapView.onResume() // needed to get the map to display immediately

        mMapView.getMapAsync(this)

        return rootView
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            mPlaceDetectionClient = Places.getPlaceDetectionClient(it)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }

        injectDependencies()
        presenter.setView(this)

        swipeContainerPlaces.setOnRefreshListener(this)

        fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startAddPlace()
            }
        })
    }

    override fun showNotification(rm: RemoteMessage) {
        var nf = NotificationFilter(activity!!,rm)
        nf.chat()
        nf.groupUser()
        nf.groupRemoved()
        nf.meetingModified()
        nf.meetingRemoved()
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun setData(places: ArrayList<Place>) {
        mMap?.clear()

        for (place in places) {
            val markerOptions = MarkerOptions()
            markerOptions.position(LatLng(place.lat, place.long))
            markerOptions.title(place.name)

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            mMap?.addMarker(markerOptions)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0

        mMap?.setBuildingsEnabled(true)
        mMap?.setIndoorEnabled(true)
        mMap?.setTrafficEnabled(true)

        val mUiSettings = mMap?.getUiSettings()
        mUiSettings?.setZoomControlsEnabled(true)
        mUiSettings?.setCompassEnabled(true)
        mUiSettings?.setMyLocationButtonEnabled(true)
        mUiSettings?.setScrollGesturesEnabled(true)
        mUiSettings?.setZoomGesturesEnabled(true)
        mUiSettings?.setTiltGesturesEnabled(true)
        mUiSettings?.setRotateGesturesEnabled(true)

        mMap?.setMyLocationEnabled(true)
        mMap?.setOnMarkerClickListener(this)

        bulidGoogleApiClient()
    }

    @Synchronized protected fun bulidGoogleApiClient() {
        context?.let {
            client = GoogleApiClient.Builder(it)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build()
            client.connect()
        }
    }



    override fun onConnectionFailed(p0: ConnectionResult) {}

    @SuppressLint("RestrictedApi")
    override fun onConnected(p0: Bundle?) {
        val mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(1000)
        mLocationRequest.setFastestInterval(1000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        activity?.let {
            if (ContextCompat.checkSelfPermission(it,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(presenter.getRegion(), 12f))
                            presenter.loadPlacesData()
                        }
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.item_marker)

        val name = dialog.findViewById<TextView>(R.id.itemMarkerTVtitle)
        val address = dialog.findViewById<TextView>(R.id.itemMarkerTVaddress)

        val place = dialog.findViewById<ImageView>(R.id.itemMarkerIVplace)
        val games = dialog.findViewById<ImageView>(R.id.itemMarkerIVgames)
        val meetings = dialog.findViewById<ImageView>(R.id.itemMarkerIVmeetings)

        var actualMarker = Place()

        for (markerNow in presenter.getPlacesData()) {
            if (marker?.title.equals(markerNow.name)) {

                actualMarker = markerNow

                name.text = markerNow.name
                address.text = markerNow.address

                if(!markerNow.photo.equals("url")) {
                    Glide.with(this)
                            .load(markerNow.photo)
                            .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                            .into(place)
                }
            }
        }

        place.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val int = Intent(context,PlaceDetailActivity::class.java)
                int.putExtra("id",actualMarker.id)
                startActivityForResult(int,1)
            }
        })

        place.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                presenter.getUserProfile()?.let {
                    if (actualMarker.ownerId.equals(it.id)) {
                        context?.let { ctx ->
                            DialogFactory.buildConfirmDialog(ctx, getString(R.string.placesDialogText), Runnable {
                                presenter.removePlace(actualMarker)
                                marker?.remove()
                                dialog.dismiss()
                            }).show()
                        }
                    } else {
                        showError(R.string.placesErrorUnableRemoveNoCreator)
                    }
                }
                return true
            }
        })

        games.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val int = Intent(context,GamesActivity::class.java)
                int.putExtra("kind","place-"+actualMarker.id)
                startActivity(int)
            }
        })

        meetings.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val int = Intent(context,TabActivity::class.java)
                int.putExtra("tab",0)
                int.putExtra("kind","place-"+actualMarker.id)
                startActivity(int)
            }
        })

        dialog.show()

        return true

    }

    override fun onConnectionSuspended(p0: Int) {}

    override fun showError(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarMaps?.setVisibility(isLoading)
        mapView?.setVisibility(!isLoading)
    }

    fun startAddPlace(){
        val intent = Intent(context, AddPlaceActivity::class.java)
        intent.putExtra("id","")
        startActivityForResult(intent,1)
    }

    override fun onRefresh() {
        presenter.loadPlacesData()
        swipeContainerPlaces.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            showSuccess(R.string.placesSuccessAdding)
            presenter.setPlacesData()
        }
    }
}
