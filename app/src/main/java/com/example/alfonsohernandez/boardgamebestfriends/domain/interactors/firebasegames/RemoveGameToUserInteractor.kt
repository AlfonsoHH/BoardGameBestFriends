package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import io.reactivex.Completable

interface RemoveGameToUserInteractor {
    fun removeFirebaseDataGameToUser(userId: String, gameId: String): Completable
}