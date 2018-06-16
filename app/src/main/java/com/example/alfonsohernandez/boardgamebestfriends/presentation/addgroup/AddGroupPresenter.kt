package com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.AddGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.ModifyGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage.SaveImageFirebaseStorageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetAllUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserFromMailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri.GetPathFromUriInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePushPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.profile.ProfileContract
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class AddGroupPresenter @Inject constructor(private val fcmHandler: FCMHandler,
                                            private val getUserProfileInteractor: GetUserProfileInteractor,
                                            private val paperGroupsInteractor: PaperGroupsInteractor,
                                            private val getAllUsersInteractor: GetAllUsersInteractor,
                                            private val getSingleUserFromMailInteractor: GetSingleUserFromMailInteractor,
                                            private val addGroupInteractor: AddGroupInteractor,
                                            private val modifyGroupInteractor: ModifyGroupInteractor,
                                            private val saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
                                            private val getPathFromUriInteractor: GetPathFromUriInteractor,
                                            private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor
                                            ) : AddGroupContract.Presenter,
                                                BasePushPresenter<AddGroupContract.View>() {

    private val TAG = "AddGroupPresenter"

    var userList = arrayListOf<User>()
    var groupUserList = arrayListOf<User>()

    var compositeDisposable = CompositeDisposable()

    fun unsetView() {
        this.view = null
        compositeDisposable.dispose()
    }

    fun setView(view: AddGroupContract.View?) {
        this.view = view
        getUserProfile()?.let {
            view?.setFriend(it)
            groupUserList.add(it)
        }
        getAllUser()
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

    fun getAllUser() {
        view?.showProgress(true)
        compositeDisposable.add(getAllUsersInteractor
                .getFirebaseDataAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    for (h in it.children) {
                        userList.add(h.getValue(User::class.java)!!)
                    }
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.addGroupErrorMembers)
                }))
    }

    override fun getGroupData(groupId: String) {
        paperGroupsInteractor.get(groupId)?.let {
            view?.setData(it)
        }
    }

    override fun getFriendData(email: String) {
        view?.showProgress(true)
        compositeDisposable.add(getSingleUserFromMailInteractor
                .getFirebaseDataSingleUserFromMail(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    var alreadyExist = false
                    for (member in userList) {
                        if (member.email.equals(email))
                            alreadyExist = true
                    }
                    if (alreadyExist) {
                        var actualUser = User()
                        for (h in it.children) {
                            actualUser = h.getValue(User::class.java)!!
                        }
                        var alreadyAdded = false
                        for (user in groupUserList) {
                            if (email.equals(user.email))
                                alreadyAdded = true
                        }
                        if (!alreadyAdded) {
                            view?.setFriend(actualUser)
                            groupUserList.add(actualUser)
                        } else {
                            view?.showError(R.string.addGroupErrorAlready)
                        }
                    } else {
                        view?.showError(R.string.addGroupErrorBuddy)
                    }
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.addGroupErrorBuddy)
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.addGroupErrorBuddy)
                }))
    }

    override fun saveImage(group: Group, data: Bitmap, modify: Boolean, userList: ArrayList<String>?) {
        view?.showProgress(true)
        if (userList != null || modify) {
            val baos = ByteArrayOutputStream()
            data.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataArray = baos.toByteArray()
            val key = addGroupInteractor.getKey()
            saveImageFirebaseStorageInteractor
                    .addFirebaseDataImage(key + ".jpg", dataArray)
                    .subscribe({
                        group.photo = it.downloadUrl.toString()
                        if (userList != null) {
                            saveGroupData(key, group, userList)
                        } else {
                            modifyGroupData(group)
                        }
                    }, {
                        view?.showError(R.string.addGroupErrorImage)
                    })
        } else {
            modifyGroupData(group)
        }
    }

    fun saveGroupData(key: String, group: Group, userList: ArrayList<String>) {
        addGroupInteractor
                .addFirebaseDataGroup(key, group, userList)
                .subscribe({
                    firebaseEvent("Adding group", TAG)
                    paperGroupsInteractor.add(group)
                    view?.finishAddGroup()
                }, {
                    view?.showError(R.string.addGroupErrorAddingGroup)
                })
    }

    fun modifyGroupData(group: Group) {
        modifyGroupInteractor
                .modifyFirebaseDataGroup(group.id, group)
                .subscribe({
                    firebaseEvent("Modifying place", TAG)
                    paperGroupsInteractor.update(group)
                    view?.finishAddGroup()
                }, {
                    view?.showError(R.string.addGroupErrorModify)
                })
    }

    override fun getRealPathFromURI(contentUri: Uri): String {
        return getPathFromUriInteractor.getPathFromUri(contentUri)
    }
}