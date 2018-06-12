package com.example.alfonsohernandez.boardgamebestfriends.presentation.profile

import android.app.Notification
import android.graphics.Bitmap
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetUserPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage.SaveImageFirebaseStorageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.AddUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri.GetPathFromUriInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePushPresenter
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class ProfilePresenter @Inject constructor(//private val fcmHandler: FCMHandler,
        private val saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
        private val getPathFromUriInteractor: GetPathFromUriInteractor,
        private val saveUserProfileInteractor: SaveUserProfileInteractor,
        private val addUserInteractor: AddUserInteractor,
        private val getUserProfileInteractor: GetUserProfileInteractor,
        private val paperGroupsInteractor: PaperGroupsInteractor,
        private val paperMeetingsInteractor: PaperMeetingsInteractor,
        private val paperPlacesInteractor: PaperPlacesInteractor,
        private val paperRegionsInteractor: PaperRegionsInteractor,
        private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor) : ProfileContract.Presenter,
        //BasePushPresenter<ProfileContract.View>() {
        BasePresenter<ProfileContract.View>() {


    private val TAG = "ProfilePresenter"
    lateinit var myPlace: Place

    fun setView(view: ProfileContract.View?) {
        this.view = view
        getUserProfile()?.let{
            view?.setData(it)
            getRegionData(it.regionId)
            firebaseEvent("Showing profile", TAG)
        }
        //fcmHandler.push = this
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id, activityName)
    }

    override fun saveImage(data: Bitmap){
        getUserProfile()?.let {userNotNull ->
            view?.showProgress(true)
            val baos = ByteArrayOutputStream()
            data.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataArray = baos.toByteArray()
            saveImageFirebaseStorageInteractor
                    .addFirebaseDataImage(userNotNull.id + ".jpg", dataArray)
                    .subscribe({
                        val user = userNotNull
                        user.photo = it.downloadUrl.toString()
                        saveUser(user)
                        view?.showProgress(false)
                    }, {
                        view?.showError(R.string.profileErrorImage)
                        view?.showProgress(false)
                    })
        }
    }

    fun saveUser(user: User){
        view?.showProgress(true)
        addUserInteractor
                .addFirebaseDataUser(user.id, user)
                .subscribe({
                    saveUserProfileInteractor.save(user)
                    view?.showSuccess(R.string.profileSuccessModify)
                    view?.showProgress(false)
                }, {
                    view?.showError(R.string.profileErrorSaveUser)
                    view?.showProgress(false)
                })
    }

    override fun getRealPathFromURI(contentUri: Uri): String {
        return getPathFromUriInteractor.getPathFromUri(contentUri)
    }

    override fun cleanPaper() {
        paperGroupsInteractor.clear()
        paperMeetingsInteractor.clear()
        paperPlacesInteractor.clear()
    }

    override fun myPlaceOnActualRegion() {
        var myPlaceRegionExist = false
        for (place in paperPlacesInteractor.all()) {
            if (!place.openPlace) {
                myPlaceRegionExist = true
                myPlace = place
            }
        }
        if (myPlaceRegionExist)
            view?.startPlaceDetail(myPlace.id)
        else
            view?.startAddMyPlaceDialog()
    }

    override fun getRegionData(regionId: String) {
        paperRegionsInteractor.get(regionId)?.let{
            view?.setRegionData(it)
        }
    }
}