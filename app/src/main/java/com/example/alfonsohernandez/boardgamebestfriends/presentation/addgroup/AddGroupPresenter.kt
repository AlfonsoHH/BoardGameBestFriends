package com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.AddGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.ModifyGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetAllUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetGroupUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserFromMailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class AddGroupPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                            private val getAllUsersInteractor: GetAllUsersInteractor,
                                            private val getSingleUserFromMailInteractor: GetSingleUserFromMailInteractor,
                                            private val getSingleGroupInteractor: GetSingleGroupInteractor,
                                            private val addGroupInteractor: AddGroupInteractor,
                                            private val modifyGroupInteractor: ModifyGroupInteractor,
                                            private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddGroupContract.Presenter {

    private val TAG = "AddGroupPresenter"

    var userList = arrayListOf<User>()
    var groupUserList = arrayListOf<User>()

    private var view: AddGroupContract.View? = null

    fun setView(view: AddGroupContract.View?) {
        this.view = view
        view?.addFriend(getProfileData()!!)
        groupUserList.add(getProfileData()!!)
        getAllUser()
    }

    override fun getProfileData(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getAllUser() {
        getAllUsersInteractor
                .getFirebaseDataAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        userList.add(h.getValue(User::class.java)!!)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorMembers()
                })
    }

    override fun getGroupData(groupId: String) {
        view?.showProgressBar(true)
        getSingleGroupInteractor
                .getFirebaseDataSingleGroup(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setData(it.getValue(Group::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorBuddy()
                })
    }

    override fun getFriendData(email: String) {
        view?.showProgressBar(true)
        getSingleUserFromMailInteractor
                .getFirebaseDataSingleUserFromMail(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var alreadyExist = false
                    for(member in userList){
                        if(member.email.equals(email))
                            alreadyExist = true
                    }
                    if (alreadyExist) {
                        var actualUser = it.getValue(User::class.java)
                        var alreadyAdded = false
                        for(user in groupUserList){
                            if(email.equals(user.email))
                                alreadyAdded = true
                        }
                        if(!alreadyAdded){
                            Log.d(TAG,actualUser!!.email)
                            view?.addFriend(actualUser!!)
                            groupUserList.add(actualUser!!)
                        }else{
                            view?.showErrorAlready()
                        }
                    }else{
                        view?.showErrorBuddy()
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorBuddy()
                })
    }

    override fun saveGroupData(group: Group, userList: ArrayList<String>) {
        addGroupInteractor
                .addFirebaseDataGroup(group, userList)
                .subscribe({
                    firebaseEvent("Adding group",TAG)
                    view?.finishAddGroup()
                },{
                    view?.showErrorAdding()
                })
    }

    override fun modifyGroupData(group: Group) {
        modifyGroupInteractor
                .modifyFirebaseDataGroup(group.id,group)
                .subscribe({
                    firebaseEvent("Modifying place", TAG)
                    view?.finishAddGroup()
                },{
                    view?.showErrorModify()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun getUrlFromPhoto(cursor: Cursor?): String {

        cursor!!.moveToFirst()

        var imagePath = cursor.getString(cursor.getColumnIndex(arrayOf(MediaStore.Images.Media.DATA)[0]))
        var options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        var stream = ByteArrayOutputStream()

        var bitmapRedux = BitmapFactory.decodeFile(imagePath, options)
        bitmapRedux.compress(Bitmap.CompressFormat.PNG, 100, stream)

        var url = Base64.encodeToString(stream.toByteArray(), 0)

        cursor.close()

        return url
    }

    override fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null)
                cursor.close()
        }
    }
}