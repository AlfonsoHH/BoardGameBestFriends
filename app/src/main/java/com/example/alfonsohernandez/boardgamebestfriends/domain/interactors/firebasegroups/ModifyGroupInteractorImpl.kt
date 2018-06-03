package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GroupsRepository
import io.reactivex.Completable

class ModifyGroupInteractorImpl (private val groupsRepository: GroupsRepository): ModifyGroupInteractor{
    override fun modifyFirebaseDataGroup(groupId: String, group: Group): Completable {
        return Completable.create {
            try {
                groupsRepository.modifyGroup(groupId,group)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}