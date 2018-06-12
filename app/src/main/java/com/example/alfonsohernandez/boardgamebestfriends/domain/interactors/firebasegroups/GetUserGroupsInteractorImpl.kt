package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GroupsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Flowable
import io.reactivex.Maybe

class GetUserGroupsInteractorImpl (private val groupsRepository: GroupsRepository): GetUserGroupsInteractor{
    override fun getFirebaseDataUserGroups(userId: String): Maybe<DataSnapshot> {
        return groupsRepository.getUserGroupsRx(userId)
    }
}