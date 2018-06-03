package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GroupsRepository
import io.reactivex.Completable

class AddGroupInteractorImpl (private val groupsRepository: GroupsRepository): AddGroupInteractor {
    override fun addFirebaseDataGroup(group: Group, userList: ArrayList<String>): Completable {
        return Completable.create {
            try {
                groupsRepository.addGroup(group, userList)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}