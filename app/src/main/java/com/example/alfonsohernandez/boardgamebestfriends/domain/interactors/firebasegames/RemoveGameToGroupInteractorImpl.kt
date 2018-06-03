package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import io.reactivex.Completable

class RemoveGameToGroupInteractorImpl (private val gamesRepository: GamesRepository): RemoveGameToGroupInteractor {
    override fun removeFirebaseDataGameToGroup(groupId: String, gameId: String): Completable {
        return Completable.create {
            try {
                gamesRepository.removeGameToGroup(groupId,gameId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }

    }
}