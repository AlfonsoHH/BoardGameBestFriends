package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import io.reactivex.Completable

interface RemoveGroupInteractor {
    fun removeFirebaseDataGroup(groupId: String): Completable
}