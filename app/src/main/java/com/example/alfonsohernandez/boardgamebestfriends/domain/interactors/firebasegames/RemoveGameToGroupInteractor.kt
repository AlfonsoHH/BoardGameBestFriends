package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import io.reactivex.Completable

interface RemoveGameToGroupInteractor {
    fun removeFirebaseDataGameToGroup(groupId: String, gameId: String): Completable
}