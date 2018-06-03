package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GroupsRepository
import io.reactivex.Completable

class RemoveGroupToUserInteractorImpl (private val groupsRepository: GroupsRepository): RemoveGroupToUserInteractor{
    override fun removeFirebaseDataGroupToUser(userId:String, groupId: String): Completable {
        return Completable.create {
            try {
                groupsRepository.removeGroupToUser(userId,groupId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}