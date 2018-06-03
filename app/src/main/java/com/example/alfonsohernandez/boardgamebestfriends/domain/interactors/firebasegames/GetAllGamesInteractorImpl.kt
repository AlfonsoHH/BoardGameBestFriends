package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetAllGamesInteractorImpl(private val gamesRepository: GamesRepository): GetAllGamesInteractor {
    override fun getFirebaseDataAllGames(): Maybe<DataSnapshot> {
        return gamesRepository.getAllGamesRx()
    }
}