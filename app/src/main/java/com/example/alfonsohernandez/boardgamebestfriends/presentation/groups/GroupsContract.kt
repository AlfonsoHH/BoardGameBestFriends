package com.example.alfonsohernandez.boardgamebestfriends.presentation.groups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface GroupsContract {

    interface View {

        fun clearData()
        fun setData(group: Group)
        fun setupRecycler()
        fun removeGroup(group: Group)
        fun itemDetail(id: String)

        fun showErrorLoading()
        fun showErrorRemovingUser()
        fun showErrorRemovingGroup()


        fun successRemovingUser()
        fun successRemovingGroup()
        fun successAdding()

        fun showProgressBar(isLoading: Boolean)

    }

    interface Presenter {

        fun getUserProfile(): User?
        fun getGroupsData()
        fun getSingleGroupData(groupId: String)

        fun removeGroup(group: Group)
        fun removeUserFromGroup(group: Group)

    }

}