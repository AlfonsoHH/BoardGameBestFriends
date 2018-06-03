package com.example.alfonsohernandez.boardgamebestfriends.presentation.profile

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetUserPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class ProfilePresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                           private val getUserPlacesInteractor: GetUserPlacesInteractor,
                                           private val getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                           private val getRegionInteractor: GetRegionInteractor,
                                           private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): ProfileContract.Presenter {

    private val TAG = "ProfilePresenter"
    lateinit var myPlace: Place

    var listPlaces = arrayListOf<Place>()

    private var view: ProfileContract.View? = null

    fun setView(view: ProfileContract.View?) {
        this.view = view
        view?.setData(getUserProfile())
        getRegionData(getUserProfile()!!.regionId)
        getUserPlaces()
        firebaseEvent("Showing profile",TAG)
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getUserPlaces() {
        view?.showProgressBar(true)
        getUserPlacesInteractor
                .getFirebaseDataUserPlaces(getUserProfile()!!.regionId,getUserProfile()!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for(h in it.children) {
                        getSinglePlace(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorPlaces()
                })
    }

    override fun getSinglePlace(placeId: String){
        view?.showProgressBar(true)
        getSinglePlaceInteractor
                .getFirebaseDataSinglePlace(getUserProfile()!!.regionId,placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    listPlaces.add(it.getValue(Place::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorPlaces()
                })
    }

    override fun myPlaceOnActualRegion(){
        var myPlaceRegionExist = false

        for (h in listPlaces) {
            if(!h.openPlace) {
                myPlaceRegionExist = true
                myPlace = h
            }
        }
        if(myPlaceRegionExist)
            view?.startPlaceDetail(myPlace.id)
        else
            view?.startAddMyPlaceDialog()
    }

    override fun getRegionData(regionId: String) {
        view?.showProgressBar(true)
        getRegionInteractor
                .getFirebaseDataSingleRegion(regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setRegionData(it.getValue(Region::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorRegion()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }
}