package com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace

import android.graphics.Bitmap
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.AddPlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.ModifyPlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage.SaveImageFirebaseStorageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getlatlong.GetLatLongInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri.GetPathFromUriInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class AddPlacePresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                            private val paperPlacesInteractor: PaperPlacesInteractor,
                                            private val paperRegionsInteractor: PaperRegionsInteractor,
                                            private val getLatLongInteractor: GetLatLongInteractor,
                                            private val addPlaceInteractor: AddPlaceInteractor,
                                            private val modifyPlaceInteractor: ModifyPlaceInteractor,
                                            private val saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
                                            private val getPathFromUriInteractor: GetPathFromUriInteractor,
                                            private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddPlaceContract.Presenter, BasePresenter<AddPlaceContract.View>() {
    private val TAG = "AddPlacePresenter"

    lateinit var region: Region

    fun setView(view: AddPlaceContract.View?) {
        this.view = view
        view?.setSpinnerData()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun getPlace(placeId: String) {
        paperPlacesInteractor.get(placeId)?.let {
            view?.setData(it)
        }
    }

    override fun saveImage(place: Place, data: Bitmap, modifiedPhoto: Boolean, actionModify: Boolean) {
        view?.showProgress(true)
        if (modifiedPhoto) {
            val baos = ByteArrayOutputStream()
            data.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataArray = baos.toByteArray()
            val key = addPlaceInteractor.getKey()
            saveImageFirebaseStorageInteractor
                    .addFirebaseDataImage(key + ".jpg", dataArray)
                    .subscribe({
                        view?.showProgress(false)
                        place.photo = it.downloadUrl.toString()
                        if(actionModify)
                            modifyPlace(place)
                        else
                            savePlace(key,place)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.addPlaceErrorImage)
                    })
        }else{
            modifyPlace(place)
        }
    }

    override fun savePlace(key: String, place: Place) {
        view?.showProgress(true)
        getLatLongFromAddress(place.address)?.let {
            if (place.lat == 0.0 && place.long == 0.0) {
                var ll = it
                place.lat = ll.latitude
                place.long = ll.longitude
            }
        }
        if(place.lat == 0.0 && place.long == 0.0) {
            view?.showError(R.string.addPlaceErrorAddress)
        }else {
            getUserProfile()?.let { user ->
                addPlaceInteractor
                        .addFirebaseDataPlace(key, user.regionId, place)
                        .subscribe({
                            firebaseEvent("Adding place", TAG)
                            paperPlacesInteractor.add(place)
                            view?.finishAddPlace()
                            view?.showProgress(false)
                        }, {
                            view?.showError(R.string.addPlaceErrorAddingPlace)
                            view?.showProgress(false)
                        })
            }
        }
    }

    override fun modifyPlace(place: Place) {
        getLatLongFromAddress(place.address)?.let {
            if (place.lat == 0.0 && place.long == 0.0) {
                var ll = it
                place.lat = ll.latitude
                place.long = ll.longitude
            }
        }
        if(place.lat == 0.0 && place.long == 0.0) {
            view?.showError(R.string.addPlaceErrorAddress)
        }else{
            getUserProfile()?.let { user ->
                modifyPlaceInteractor
                        .modifyFirebaseDataPlace(user.regionId, place.id, place)
                        .subscribe({
                            firebaseEvent("Modifying place", TAG)
                            paperPlacesInteractor.update(place)
                            view?.finishAddPlace()
                        }, {
                            view?.showError(R.string.addPlaceErrorModifying)
                        })
            }
        }
    }

    override fun getLatLongFromAddress(address: String): LatLng? {
        val user = getUserProfile()
        if(user != null) {
            val region = paperRegionsInteractor.get(user.regionId)
            if(region != null)
                return getLatLongInteractor.getLatLong(region.city, region.country, address)
            else
                return LatLng(0.0,0.0)
        }else{
            return LatLng(0.0,0.0)
        }
    }

    override fun getRealPathFromURI(contentUri: Uri): String {
        return getPathFromUriInteractor.getPathFromUri(contentUri)
    }
}