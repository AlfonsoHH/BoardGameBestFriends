package com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface AddGroupContract {

    interface View {

        fun setData(group: Group)
        fun setupRecycler()
        fun addFriend(user: User)
        fun finishAddGroup()

        fun showErrorBuddy()
        fun showErrorAdding()
        fun showErrorMembers()
        fun showErrorAlready()
        fun showErrorModify()
        fun showErrorEmpty()

        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter {

        fun getProfileData(): User?

        fun getAllUser()
        fun getFriendData(userId: String)
        fun getGroupData(groupId: String)
        fun saveGroupData(group: Group, userList: ArrayList<String>)
        fun modifyGroupData(group: Group)
        fun firebaseEvent(id: String, activityName: String)

        fun getUrlFromPhoto(cursor: Cursor?): String
        fun getRealPathFromURI(context: Context, contentUri: Uri): String
    }

}