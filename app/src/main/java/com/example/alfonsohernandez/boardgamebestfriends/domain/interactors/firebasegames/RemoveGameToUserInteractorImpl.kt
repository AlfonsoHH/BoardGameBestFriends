package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import io.reactivex.Completable

class RemoveGameToUserInteractorImpl (private val gamesRepository: GamesRepository): RemoveGameToUserInteractor {
    override fun removeFirebaseDataGameToUser(userId: String, gameId: String): Completable {
        return Completable.create {
            try {
                gamesRepository.removeGameToUser(userId,gameId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}