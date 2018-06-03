package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetPlaceGamesInteractor {
    fun getFirebaseDataPlaceGames(placeId: String): Maybe<DataSnapshot>
}