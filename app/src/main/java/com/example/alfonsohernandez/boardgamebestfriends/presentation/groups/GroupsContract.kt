package com.example.alfonsohernandez.boardgamebestfriends.presentation.groups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface GroupsContract {

    interface View: BaseNotificationView {

        fun setData(groups: ArrayList<Group>)

    }

    interface Presenter {

        fun updateGroupsFromCache()
        fun removeGroup(group: Group)
        fun removeUserFromGroup(group: Group)

    }

}