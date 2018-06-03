package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import io.reactivex.Completable

class AddGameToPlaceInteractorImpl (private val gamesRepository: GamesRepository): AddGameToPlaceInteractor {
    override fun AddFirebaseDataGameToPlace(placeId: String, gameId: String): Completable {
        return Completable.create {
            try {
                gamesRepository.addGameToPlace(placeId,gameId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}