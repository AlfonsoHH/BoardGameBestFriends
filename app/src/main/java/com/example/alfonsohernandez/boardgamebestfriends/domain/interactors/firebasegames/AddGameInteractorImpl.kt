package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import io.reactivex.Completable

class AddGameInteractorImpl(private val gamesRepository: GamesRepository) : AddGameInteractor {
    override fun AddFirebaseDataGame(game: Game): Completable {
        return Completable.create {
            try {
                gamesRepository.addGame(game)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}