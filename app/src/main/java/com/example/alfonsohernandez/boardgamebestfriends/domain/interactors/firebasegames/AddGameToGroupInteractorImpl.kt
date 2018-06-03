package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import io.reactivex.Completable

class AddGameToGroupInteractorImpl (private val gamesRepository: GamesRepository): AddGameToGroupInteractor {
    override fun AddFirebaseDataGameToGroup(groupId: String, gameId: String): Completable {
        return Completable.create {
            try {
                gamesRepository.addGameToGroup(groupId,gameId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }

    }
}