package com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface AddGroupContract {

    interface View: BaseView {

        fun setData(group: Group)
        fun setFriend(user: User)
        fun finishAddGroup()
        fun setPhotoImage(image: Any)

    }

    interface Presenter {

        fun getGroupData(groupId: String)
        fun getFriendData(email: String)
        fun saveImage(group: Group, data: Bitmap, modify: Boolean, userList: ArrayList<String>?)
        fun getRealPathFromURI(contentUri: Uri): String

    }

}