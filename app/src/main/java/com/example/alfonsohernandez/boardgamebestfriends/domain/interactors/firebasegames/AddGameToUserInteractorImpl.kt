package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import io.reactivex.Completable

class AddGameToUserInteractorImpl(private val gamesRepository: GamesRepository): AddGameToUserInteractor {
    override fun addFirebaseDataGameToUser(userId: String, gameId: String): Completable {
        return Completable.create {
            try {
                gamesRepository.addGameToUser(userId,gameId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }

    }
}