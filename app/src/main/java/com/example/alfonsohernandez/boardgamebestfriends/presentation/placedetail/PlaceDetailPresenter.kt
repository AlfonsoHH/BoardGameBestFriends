package com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlaceDetailPresenter @Inject constructor(private val userProfileInteractor: GetUserProfileInteractor,
                                               private val getRegionInteractor: GetRegionInteractor,
                                               private val getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                               private val firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): PlaceDetailContract.Presenter {

    private val TAG = "PlaceDetailPresenter"

    private var view: PlaceDetailContract.View? = null

    fun setView(view: PlaceDetailContract.View?, placeId: String) {
        this.view = view
        view?.showProgressBar(true)
        getPlaceData(userProfileInteractor.getProfile()!!.regionId,placeId)
        firebaseAnalyticsInteractor.sendingDataFirebaseAnalytics("Showing place",TAG)
    }

    override fun getUserProfile(): User? {
        return userProfileInteractor.getProfile()
    }

    override fun getRegionData(regionId: String, place: Place) {
        view?.showProgressBar(true)
        getRegionInteractor
                .getFirebaseDataSingleRegion(regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var region = it.getValue(Region::class.java)
                    view?.setData(place,region!!.city + ", " + region.country)
                    for (i in 1..3) {
                        when(i) {
                            1 -> {
                                view?.setRuleData(place.firstRuleId,i)
                            }
                            2 -> {
                                view?.setRuleData(place.secondRuleId,i)
                            }
                            3 -> {
                                view?.setRuleData(place.thirdRuleId,i)
                            }
                        }
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorRegion()
                })
    }

    override fun getPlaceData(regionId: String, placeId: String) {
        getSinglePlaceInteractor
                .getFirebaseDataSinglePlace(regionId,placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    getRegionData(regionId,it.getValue(Place::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorPlaces()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        firebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }
}