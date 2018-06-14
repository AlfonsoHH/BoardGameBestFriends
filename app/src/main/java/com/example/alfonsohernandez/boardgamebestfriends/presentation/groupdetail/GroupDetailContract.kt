package com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

interface GroupDetailContract {

    interface View: BaseNotificationView {

        fun setGroupData(group: Group)
        fun setFriendData(user: User)

    }

    interface Presenter {

        fun getUserProfile(): User?

        fun firebaseEvent(id: String, activityName: String)

        fun getFriendFromMailData(email: String)
        fun saveNewMember(user: User, groupId: String)

    }

}