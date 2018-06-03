package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GroupsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetSingleGroupInteractorImpl (private val groupsRepository: GroupsRepository): GetSingleGroupInteractor{
    override fun getFirebaseDataSingleGroup(groupId: String): Maybe<DataSnapshot> {
        return groupsRepository.getSingleGroupRx(groupId)
    }
}