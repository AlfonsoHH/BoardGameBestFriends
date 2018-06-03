package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetUserGamesInteractor {
    fun getFirebaseDataUserGames(userId: String): Maybe<DataSnapshot>
}