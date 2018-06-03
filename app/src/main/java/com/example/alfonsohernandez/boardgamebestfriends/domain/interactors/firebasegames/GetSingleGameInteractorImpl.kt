package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetSingleGameInteractorImpl (private val gamesRepository: GamesRepository): GetSingleGameInteractor{
    override fun getFirebaseDataSingleGame(gameId: String): Maybe<DataSnapshot> {
        return gamesRepository.getSingleGameRx(gameId)
    }
}