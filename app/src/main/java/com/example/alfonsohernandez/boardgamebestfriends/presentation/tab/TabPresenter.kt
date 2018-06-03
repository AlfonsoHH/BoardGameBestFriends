package com.example.alfonsohernandez.boardgamebestfriends.presentation.tab

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.ModifyUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class TabPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                       private val getAllRegionInteractor: GetAllRegionInteractor,
                                       private val modifyUserInteractor: ModifyUserInteractor,
                                       private val saveUserProfileInteractor: SaveUserProfileInteractor,
                                       private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): TabContract.Presenter {

    private var view: TabContract.View? = null
    private val TAG: String = "TabPresenter"

    var regionList = arrayListOf<Region>()

    fun setView(view: TabContract.View?) {
        this.view = view
        view?.setData()
        loadRegions()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun modifyUserInFirebaseDB(userId: String, user: User) {
        modifyUserInteractor
                .modifyFirebaseDataUser(userId,user)
                .subscribe({
                    saveUserInPaper(user)
                },{
                    view?.showErrorSavingUser()
                })
    }

    override fun saveUserInPaper(user: User) {
        saveUserProfileInteractor
                .save(user)
                .subscribe({
                    firebaseEvent("Login in", TAG)
                    view?.successChangingRegion()
                },{
                    view?.showErrorSavingUser()
                })
    }

    override fun loadRegions() {
        getAllRegionInteractor
                .getFirebaseDataAllRegions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.successLoadingRegions()
                    for (h in it.children) {
                        regionList.add(h.getValue(Region::class.java)!!)
                    }
                    regionList = ArrayList(regionList.sortedBy { it.city })
                },{
                    view?.showErrorLoadingRegions()
                })
    }

    override fun getCountryList(): ArrayList<String> {
        var countryList = arrayListOf<String>()
        for(region in regionList){
            var exist = false
            for(country in countryList) {
                if (country.equals(region.country))
                    exist = true
            }
            if(!exist)
                countryList.add(region.country)
        }
        return countryList
    }

    override fun getCityList(countryName: String): ArrayList<String> {
        var cityList = arrayListOf<String>()
        for(region in regionList){
            if(region.country.equals(countryName))
                cityList.add(region.city)
        }
        return cityList
    }

    override fun getRegionId(cityName: String): String {
        var regionId = ""
        for(region in regionList){
            if(region.city.equals(cityName))
                regionId = region.id
        }
        return regionId
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

}