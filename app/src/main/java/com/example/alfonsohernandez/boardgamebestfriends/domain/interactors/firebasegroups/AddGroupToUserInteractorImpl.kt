package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GroupsRepository
import io.reactivex.Completable

class AddGroupToUserInteractorImpl (private val groupsRepository: GroupsRepository): AddGroupToUserInteractor{
    override fun addFirebaseDataGroupToUser(userId: String, groupId: String): Completable {
        return Completable.create {
            try {
                groupsRepository.addGroupToUser(userId,groupId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}