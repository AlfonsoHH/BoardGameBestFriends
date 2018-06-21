package com.example.alfonsohernandez.boardgamebestfriends.presentation.places

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.RemovePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePushPresenter
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class PlacesPresenter @Inject constructor(private val fcmHandler: FCMHandler,
                                          private val getUserProfileInteractor: GetUserProfileInteractor,
                                          private val paperRegionsInteractor: PaperRegionsInteractor,
                                          private val paperPlacesInteractor: PaperPlacesInteractor,
                                          private val removePlaceInteractor: RemovePlaceInteractor,
                                          private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor
                                        ) : PlacesContract.Presenter,
                                            BasePushPresenter<PlacesContract.View>() {

    private val TAG = "PlacesPresenter"

    var compositeDisposable = CompositeDisposable()

    fun unsetViewFragment(){
        this.view = null
        compositeDisposable.clear()
    }

    fun unsetView(){
        this.view = null
        compositeDisposable.dispose()
    }

    fun setView(view: PlacesContract.View?) {
        this.view = view
        fcmHandler.push = this
    }

    override fun pushReceived(rm: RemoteMessage) {
        view?.showNotification(rm)
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id, activityName)
    }

    override fun getRegion(): LatLng? {
        val user = getUserProfile()
        if(user != null) {
            val region = paperRegionsInteractor.get(user.regionId)
            if(region != null)
                return LatLng(region.lat, region.long)
            else
                return LatLng(0.0, 0.0)
        }else {
            return LatLng(0.0, 0.0)
        }
    }

    override fun setPlacesData() {
        getUserProfile()?.let { user ->
            var placesList = arrayListOf<Place>()
            for (place in paperPlacesInteractor.all()) {
                if (place.openPlace)
                    placesList.add(place)
                if (!place.openPlace && place.ownerId.equals(user.id))
                    placesList.add(place)
            }
            view?.setData(placesList)
        }
    }

    override fun getPlacesData(): ArrayList<Place> {
        var placesList = arrayListOf<Place>()
        getUserProfile()?.let { user ->
            for (place in paperPlacesInteractor.all()) {
                if (place.openPlace)
                    placesList.add(place)
                if (!place.openPlace && place.ownerId.equals(user.id))
                    placesList.add(place)
            }
        }
        return placesList
    }

    override fun removePlace(place: Place) {
        getUserProfile()?.let {
            removePlaceInteractor
                    .removeFirebaseDatePlace(it.regionId, place.id)
                    .subscribe({
                        paperPlacesInteractor.remove(place.id)
                        view?.showSuccess(R.string.placesSuccessRemovingPlace)
                    }, {
                        view?.showError(R.string.placesErrorRemovingPlace)
                    })
        }
    }
}