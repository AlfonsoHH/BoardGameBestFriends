package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import io.reactivex.Completable

interface AddGameToUserInteractor {
    fun addFirebaseDataGameToUser(userId: String, gameId: String): Completable
}