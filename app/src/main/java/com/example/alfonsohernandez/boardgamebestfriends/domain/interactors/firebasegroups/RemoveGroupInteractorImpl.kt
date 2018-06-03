package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GroupsRepository
import io.reactivex.Completable

class RemoveGroupInteractorImpl (private val groupsRepository: GroupsRepository): RemoveGroupInteractor{
    override fun removeFirebaseDataGroup(groupId: String): Completable {
        return Completable.create {
            try {
                groupsRepository.removeGroup(groupId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}