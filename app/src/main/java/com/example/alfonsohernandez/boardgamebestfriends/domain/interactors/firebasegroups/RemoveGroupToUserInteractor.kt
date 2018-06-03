package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import io.reactivex.Completable

interface RemoveGroupToUserInteractor {
    fun removeFirebaseDataGroupToUser(userId:String, groupId: String): Completable
}