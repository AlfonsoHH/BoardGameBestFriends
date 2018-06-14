package com.example.alfonsohernandez.boardgamebestfriends.presentation.login

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.GetCurrentAuthUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.LoginWithCredentialsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.LoginWithEmailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.AddUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetAllUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.ModifyUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class LoginPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                         private val getCurrentAuthUserInteractor: GetCurrentAuthUserInteractor,
                                         private val loginWithEmailInteractor: LoginWithEmailInteractor,
                                         private val loginWithCredentialsInteractor: LoginWithCredentialsInteractor,
                                         private val paperRegionsInteractor: PaperRegionsInteractor,
                                         private val saveUserProfileInteractor: SaveUserProfileInteractor,
                                         private val getSingleUserInteractor: GetSingleUserInteractor,
                                         private val getAllUsersInteractor: GetAllUsersInteractor,
                                         private val addUserInteractor: AddUserInteractor,
                                         private val modifyUserInteractor: ModifyUserInteractor,
                                         private val getAllRegionInteractor: GetAllRegionInteractor,
                                         private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): LoginContract.Presenter, BasePresenter<LoginContract.View>() {

    private val TAG = "LoginPresenter"

    fun setView(view: LoginContract.View?) {
        this.view = view
        loadRegions()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getAuthUser(): FirebaseUser?{
        return getCurrentAuthUserInteractor.getFirebaseCurrentAuthUser()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun loginWithEmail(email: String, password: String){
        view?.showProgress(true)
        loginWithEmailInteractor
                .loginAuthWithMail(email,password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    getSingleUser(it.user.uid)
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.loginErrorEmail)
                })
    }

    override fun loginWithCredentials(credential: AuthCredential, fromFacebook: Boolean){
        view?.showProgress(true)
        loginWithCredentialsInteractor
                .loginAuthWithCredentials(credential)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    var user = it.user
                    if(user != null) {
                        if (it.additionalUserInfo.isNewUser)
                            view?.chooseRegion(fromFacebook)
                        else
                            hasChoosenARegion(user.uid,fromFacebook)
                    }
                },{
                    view?.showProgress(false)
                    if(fromFacebook)
                        view?.showError(R.string.loginErrorFacebook)
                    else
                        view?.showError(R.string.loginErrorGmail)
                },{
                    view?.showProgress(false)
                })
    }

    override fun loadRegions() {
        view?.showProgress(true)
        getAllRegionInteractor
                .getFirebaseDataAllRegions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    var regionList = arrayListOf<Region>()
                    for (h in it.children) {
                        regionList.add(h.getValue(Region::class.java)!!)
                    }
                    regionList = ArrayList(regionList.sortedBy { it.city })
                    paperRegionsInteractor.clear()
                    paperRegionsInteractor.addAll(regionList)
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.loginErrorLoadingRegion)
                })
    }

    override fun getRegions(): ArrayList<Region>{
        return paperRegionsInteractor.all()
    }

    override fun getRegionId(cityName: String): String {
        var regionId = ""
        for(region in paperRegionsInteractor.all()){
            if(region.city.equals(cityName))
                regionId = region.id
        }
        return regionId
    }

    fun hasChoosenARegion(userId: String, fromFacebook: Boolean){
        view?.showProgress(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    var user = it.getValue(User::class.java)!!
                    if(user.regionId.equals(""))
                        view?.chooseRegion(fromFacebook)
                    else
                        saveUserInPaper(user)
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.loginErrorLoadingUsers)
                },{
                    view?.showProgress(false)
                    view?.chooseRegion(fromFacebook)
                })
    }

    override fun getSingleUser(userId: String) {
        view?.showProgress(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    saveUserInPaper(it.getValue(User::class.java)!!)
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.loginErrorLoadingUsers)
                })
    }

    override fun getUsersData(userId: String,user: User) {
        view?.showProgress(true)
        getAllUsersInteractor
                .getFirebaseDataAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
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
                    view?.showProgress(false)
                    view?.showError(R.string.loginErrorLoadingUsers)
                })
    }

    override fun saveUserInFirebaseDB(userId: String,user: User) {
        addUserInteractor
                .addFirebaseDataUser(userId,user)
                .subscribe({
                    saveUserInPaper(user)
                },{
                    view?.showError(R.string.loginErrorSavingUser)
                })
    }

    override fun modifyUserInFirebaseDB(userId: String,user: User) {
        modifyUserInteractor
                .modifyFirebaseDataUser(userId,user)
                .subscribe({
                    saveUserInPaper(user)
                },{
                    view?.showError(R.string.loginErrorSavingUser)
                })
    }

    override fun saveUserInPaper(user: User) {
        saveUserProfileInteractor
                .save(user)
                .subscribe({
                    firebaseEvent("Login in", TAG)
                    view?.nextActivity()
                },{
                    view?.showError(R.string.loginErrorSavingUser)
                })
    }

}