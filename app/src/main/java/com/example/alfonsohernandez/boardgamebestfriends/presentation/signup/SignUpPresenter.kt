package com.example.alfonsohernandez.boardgamebestfriends.presentation.signup

import android.graphics.Bitmap
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.CreateMailUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage.SaveImageFirebaseStorageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.AddUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetAllUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri.GetPathFromUriInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import javax.inject.Inject


/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class SignUpPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                          private val getAllUsersInteractor: GetAllUsersInteractor,
                                          private val paperRegionsInteractor: PaperRegionsInteractor,
                                          private val createMailUserInteractor: CreateMailUserInteractor,
                                          private val addUserInteractor: AddUserInteractor,
                                          private val saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
                                          private val getPathFromUriInteractor: GetPathFromUriInteractor,
                                          private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): SignUpContract.Presenter, BasePresenter<SignUpContract.View>() {

    private val TAG = "SignUpPresenter"

    fun setView(view: SignUpContract.View?) {
        this.view = view
        getRegionData()
        firebaseEvent("Showing profile",TAG)
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    fun createUser(email: String, password: String){
        view?.showProgress(true)
        createMailUserInteractor
                .createAuthMailUser(email,password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    if (it.additionalUserInfo.isNewUser) {
                        view?.saveUser(it.user)
                    } else {
                        view?.showError(R.string.signUpErrorAlreadyExist)
                    }
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.signUpErrorAuthentification)
                })
    }

    override fun saveImage(name: String, user: User, data: Bitmap) {
        val baos = ByteArrayOutputStream()
        data.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val dataArray = baos.toByteArray()
        saveImageFirebaseStorageInteractor
                .addFirebaseDataImage(name + ".jpg", dataArray)
                .subscribe({
                    user.photo = it.downloadUrl.toString()
                    saveUserData(name,user)
                },{
                    view?.showError(R.string.signUpErrorImage)
                })
    }

    override fun spinnerItemChange(countrySelected: String) {
        val tempRegionList = paperRegionsInteractor.all()
        tempRegionList.filter { it -> it.country.equals(countrySelected) }
        val cityListName = arrayListOf<String>()
        for(region in tempRegionList){
            cityListName.add(region.city)
        }
        view?.setupSpinnerCity(cityListName)
    }

    override fun getUsersData(userMail: String, password: String) {
        view?.showProgress(true)
        getAllUsersInteractor
                .getFirebaseDataAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    var alreadyExist = false
                    for (h in it.children) {
                        if(h.getValue(User::class.java)?.email.equals(userMail))
                            alreadyExist = true
                    }
                    if(!alreadyExist) {
                        createUser(userMail,password)
                        firebaseEvent("Signing up", TAG)
                    }else {
                        view?.showError(R.string.signUpErrorUser)
                    }
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.signUpErrorAllUsers)
                })
    }

    override fun saveUserData(userId:String, user: User) {
        addUserInteractor
                .addFirebaseDataUser(userId,user)
                .subscribe({
                    view?.finishSignUp()
                },{
                    view?.showError(R.string.signUpErrorUser)
                })
    }

    override fun getRegionIdFromCity(city: String): String {
        val tempRegionList = paperRegionsInteractor.all()
        return tempRegionList.filter { it -> it.city.equals(city) }.get(0).id
    }

    override fun getRegionData() {
        val regionList = arrayListOf<Region>()
        val countryListName = arrayListOf<String>()
        for (actualRegion in paperRegionsInteractor.all()) {
            regionList.add(actualRegion)
            for (region in regionList) {
                if (!actualRegion.country.equals(region.country) || countryListName.size == 0) {
                    countryListName.add(actualRegion.country)
                }
            }
        }
        view?.setupSpinnerCountry(countryListName)
        spinnerItemChange(countryListName.get(0))
    }

    override fun getRealPathFromURI(contentUri: Uri): String {
        return getPathFromUriInteractor.getPathFromUri(contentUri)
    }


}