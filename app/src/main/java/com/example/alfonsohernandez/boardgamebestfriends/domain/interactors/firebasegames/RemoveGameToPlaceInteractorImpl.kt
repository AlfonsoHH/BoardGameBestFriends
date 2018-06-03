package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import io.reactivex.Completable

class RemoveGameToPlaceInteractorImpl (private val gamesRepository: GamesRepository): RemoveGameToPlaceInteractor {
    override fun RemoveFirebaseDataGameToPlace(placeId: String, gameId: String): Completable {
        return Completable.create {
            try {
                gamesRepository.removeGameToPlace(placeId,gameId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}