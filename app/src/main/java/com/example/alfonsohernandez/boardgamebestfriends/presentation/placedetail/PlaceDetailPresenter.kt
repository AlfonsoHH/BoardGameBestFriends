package com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlaceDetailPresenter @Inject constructor(private val userProfileInteractor: GetUserProfileInteractor,
                                               private val paperPlacesInteractor: PaperPlacesInteractor,
                                               private val paperRegionsInteractor: PaperRegionsInteractor,
                                               private val firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor) : PlaceDetailContract.Presenter, BasePresenter<PlaceDetailContract.View>() {

    private val TAG = "PlaceDetailPresenter"

    fun setView(view: PlaceDetailContract.View?, placeId: String) {
        this.view = view
        view?.showProgress(true)
        getData(placeId)
        firebaseAnalyticsInteractor.sendingDataFirebaseAnalytics("Showing place", TAG)
    }

    override fun getUserProfile(): User? {
        return userProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        firebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id, activityName)
    }

    override fun getDay(day: String): String {
        return day.substring(0, day.lastIndexOf("_") - 1)
    }

    override fun getHours(hours: String): String {
        return hours.substring(hours.lastIndexOf("_") + 1, hours.length)
    }

    fun getData(placeId: String) {
        paperPlacesInteractor.get(placeId)?.let {place ->
            getUserProfile()?.let {user ->
                val region = paperRegionsInteractor.get(user.regionId)
                view?.setData(place, region?.city + ", " + region?.country)
                for (i in 1..3) {
                    when (i) {
                        1 -> {
                            view?.setRuleData(place.firstRuleId, i)
                        }
                        2 -> {
                            view?.setRuleData(place.secondRuleId, i)
                        }
                        3 -> {
                            view?.setRuleData(place.thirdRuleId, i)
                        }
                    }
                }
            }
        }
        view?.showProgress(false)
    }
}