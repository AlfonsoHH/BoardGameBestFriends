package com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

interface GroupDetailContract {

    interface View {

        fun setGroupData(group: Group)
        fun setRegionData(region: Region)
        fun setFriendData(user: User)
        fun clearFriendsData()

        fun showErrorGroup()
        fun showErrorMembers()
        fun showErrorRegion()
        fun showErrorNewMember()
        fun showErrorAlreadyExist()
        fun showErrorDoesNotExist()

        fun successNewMember()
        fun successModify()

        fun showProgressBar(isLoading: Boolean)

        fun setupRecycler()

    }

    interface Presenter {

        fun getUserProfile(): User?

        fun getGroupData(groupId: String)
        fun getRegionData(regionId: String)
        fun getMembersData(groupId: String)
        fun getMemberData(memberId: String)
        fun firebaseEvent(id: String, activityName: String)

        fun getFriendData(userId: String)
        fun saveNewMember(user: User, groupId: String)

    }

}