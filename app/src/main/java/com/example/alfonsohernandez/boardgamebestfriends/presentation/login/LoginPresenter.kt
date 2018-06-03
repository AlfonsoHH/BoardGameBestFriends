package com.example.alfonsohernandez.boardgamebestfriends.presentation.login

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.AddUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetAllUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
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

class LoginPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                         private val saveUserProfileInteractor: SaveUserProfileInteractor,
                                         private val getSingleUserInteractor: GetSingleUserInteractor,
                                         private val getAllUsersInteractor: GetAllUsersInteractor,
                                         private val addUserInteractor: AddUserInteractor,
                                         private val modifyUserInteractor: ModifyUserInteractor,
                                         private val getAllRegionInteractor: GetAllRegionInteractor,
                                         private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): LoginContract.Presenter {

    private val TAG = "LoginPresenter"

    private var view: LoginContract.View? = null

    var regionList = arrayListOf<Region>()

    fun setView(view: LoginContract.View?) {
        this.view = view
        loadRegions()
    }

    override fun loadRegions() {
        view?.showProgressBar(true)
        getAllRegionInteractor
                .getFirebaseDataAllRegions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        regionList.add(h.getValue(Region::class.java)!!)
                    }
                    regionList = ArrayList(regionList.sortedBy { it.city })
                },{
                    view?.showProgressBar(false)
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

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getSingleUser(userId: String) {
        view?.showProgressBar(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    saveUserInPaper(it.getValue(User::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoadingUsers()
                })
    }

    override fun getUsersData(userId: String,user: User) {
        view?.showProgressBar(true)
        getAllUsersInteractor
                .getFirebaseDataAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var alreadyExist = false
                    for (h in it.children) {
                        if(h.getValue(User::class.java)!!.email.equals(user.email)) {
                            alreadyExist = true
                        }
                    }
                    if(!alreadyExist) {
                        saveUserInFirebaseDB(userId,user)
                    }else {
                        modifyUserInFirebaseDB(userId,user)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoadingUsers()
                })
    }

    override fun saveUserInFirebaseDB(userId: String,user: User) {
        addUserInteractor
                .addFirebaseDataUser(userId,user)
                .subscribe({
                    saveUserInPaper(user)
                },{
                    view?.showErrorSavingUser()
                })
    }

    override fun modifyUserInFirebaseDB(userId: String,user: User) {
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
                    view?.nextActivity()
                },{
                    view?.showErrorSavingUser()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }
}