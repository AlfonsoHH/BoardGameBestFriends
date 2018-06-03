package com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.AddPlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.AddPlaceToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.ModifyPlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.google.android.gms.ads.internal.gmsg.HttpClient
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class AddPlacePresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                            private val addPlaceInteractor: AddPlaceInteractor,
                                            private val addPlaceToUserInteractor: AddPlaceToUserInteractor,
                                            private val modifyPlaceInteractor: ModifyPlaceInteractor,
                                            private val getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                            private val getRegionInteractor: GetRegionInteractor,
                                            private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddPlaceContract.Presenter {
    private val TAG = "AddPlacePresenter"

    lateinit var region: Region

    private var view: AddPlaceContract.View? = null

    fun setView(view: AddPlaceContract.View?) {
        this.view = view
        view?.setSpinnerData()
        getRegion()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getPlace(placeId: String) {
        view?.showProgressBar(true)
        getSinglePlaceInteractor
                .getFirebaseDataSinglePlace(getUserProfile()!!.regionId,placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setData(it.getValue(Place::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorRules()
                })
    }

    override fun savePlace(context: Context, place: Place) {
        var user = getUserProfile()
        if(place.lat == 0.0 && place.long == 0.0) {
            place.lat = getLatLongFromAddress(context, place.address)!!.latitude
            place.long = getLatLongFromAddress(context, place.address)!!.longitude
        }
        if(place.lat == 0.0 && place.long == 0.0) {
            view?.showErrorAddress()
        }else {
            addPlaceInteractor
                    .addFirebaseDataPlace(user!!.regionId, place)
                    .subscribe({
                        firebaseEvent("Adding place", TAG)
                        addPlaceToUserInteractor
                                .addFirebaseDataPlaceToUser(user!!.regionId, user!!.id, place.id)
                                .subscribe({
                                    view?.finishAddPlace()
                                }, {
                                    view?.showErrorAddingPlaceToUser()
                                })
                    }, {
                        view?.showErrorAddingPlace()
                    })
        }
    }

    override fun modifyPlace(context: Context, place: Place) {
        var user = getUserProfile()
        if(place.lat == 0.0 && place.long == 0.0) {
            place.lat = getLatLongFromAddress(context, place.address)!!.latitude
            place.long = getLatLongFromAddress(context, place.address)!!.longitude
        }
        if(place.lat == 0.0 && place.long == 0.0) {
            view?.showErrorAddress()
        }else{
            modifyPlaceInteractor
                    .modifyFirebaseDataPlace(getUserProfile()!!.regionId, place.id, place)
                    .subscribe({
                        firebaseEvent("Modifying place", TAG)
                        view?.finishAddPlace()
                    }, {
                        view?.showErrorModify()
                    })
        }
    }

    override fun getRegion(){
        view?.showProgressBar(true)
        getRegionInteractor
                .getFirebaseDataSingleRegion(getUserProfile()!!.regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    region = it.getValue(Region::class.java)!!
                },{
                    view?.showProgressBar(false)
                    view?.showErrorRegion()
                })
    }

    override fun getLatLongFromAddress(context: Context, strAddress: String): LatLng? {
        var coder = Geocoder(context)
        var address: List<Address>
        var p1 = LatLng(0.0,0.0)

        var modifiedAddress = strAddress + " " + region.city + " " + region.country

        try {
            address = coder.getFromLocationName(modifiedAddress, 5)
            if (address == null) {
                return null
            }
            if(address.get(0)!=null) {
                var location = address.get(0)
                p1 = LatLng(location.getLatitude(), location.getLongitude())
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return p1
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun getUrlFromPhoto(cursor: Cursor?): String {

        cursor!!.moveToFirst()

        var imagePath = cursor.getString(cursor.getColumnIndex(arrayOf(MediaStore.Images.Media.DATA)[0]))
        var options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        var stream = ByteArrayOutputStream()

        var bitmapRedux = BitmapFactory.decodeFile(imagePath, options)
        bitmapRedux.compress(Bitmap.CompressFormat.PNG, 100, stream)

        var url = Base64.encodeToString(stream.toByteArray(), 0)

        cursor.close()

        return url
    }

    override fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null)
                cursor.close()
        }
    }

    override fun convertDays(days: ArrayList<Boolean>): String{
        var daysString = ""
        for(i in 0..6){
            if(days.get(i)) {
                if(i == 0){
                    if(days.get(i + 1))
                        daysString = daysString + view?.getDayString(i) + "-"
                    else
                        daysString = daysString + view?.getDayString(i) + " "
                }else if(i == 6){
                    daysString = daysString + view?.getDayString(i)
                }else {
                    if (!days.get(i - 1) && !days.get(i + 1))
                        daysString = daysString + view?.getDayString(i) + " "
                    if (!days.get(i - 1) && days.get(i + 1))
                        daysString = daysString + view?.getDayString(i) + "-"
                    if (days.get(i - 1) && !days.get(i + 1))
                        daysString = daysString + view?.getDayString(i) + " "
                }
            }
        }
        return daysString
    }

    override fun generateDayList(mon: Boolean,
                                 tue: Boolean,
                                 wed: Boolean,
                                 thu: Boolean,
                                 fri: Boolean,
                                 sat: Boolean,
                                 sun: Boolean): ArrayList<Boolean>{

        var listDaysWeek = arrayListOf<Boolean>()

        listDaysWeek.add(mon)
        listDaysWeek.add(tue)
        listDaysWeek.add(wed)
        listDaysWeek.add(thu)
        listDaysWeek.add(fri)
        listDaysWeek.add(sat)
        listDaysWeek.add(sun)

        return listDaysWeek
    }

    override fun convertHours(openMor: String, closeMor: String, aftDif: Boolean, openAft: String, closeAft: String): String{
        var hours = openMor + "-" + closeMor
        if(aftDif)
            hours = hours + " " + openAft + "-" + closeAft
        return hours
    }
}