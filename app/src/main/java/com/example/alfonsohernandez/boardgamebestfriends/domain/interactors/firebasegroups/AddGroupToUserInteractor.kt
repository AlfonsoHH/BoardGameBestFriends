package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import io.reactivex.Completable


interface AddGroupToUserInteractor {
    fun addFirebaseDataGroupToUser(userId: String, groupId: String): Completable
}