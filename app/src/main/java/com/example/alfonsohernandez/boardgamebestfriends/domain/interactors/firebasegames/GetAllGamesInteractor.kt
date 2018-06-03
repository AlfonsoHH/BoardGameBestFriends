package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetAllGamesInteractor {
    fun getFirebaseDataAllGames(): Maybe<DataSnapshot>
}