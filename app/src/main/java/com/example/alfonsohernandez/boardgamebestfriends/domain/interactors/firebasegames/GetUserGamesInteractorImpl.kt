package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetUserGamesInteractorImpl (private val gamesRepository: GamesRepository): GetUserGamesInteractor {
    override fun getFirebaseDataUserGames(userId: String): Maybe<DataSnapshot> {
        return gamesRepository.getUserGamesRx(userId)
    }
}