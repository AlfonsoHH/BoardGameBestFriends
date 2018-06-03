package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetPlaceGamesInteractorImpl (private val gamesRepository: GamesRepository): GetPlaceGamesInteractor{
    override fun getFirebaseDataPlaceGames(placeId: String): Maybe<DataSnapshot> {
        return gamesRepository.getPlaceGamesRx(placeId)
    }
}