package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import io.reactivex.Completable

interface AddGameToPlaceInteractor {
    fun AddFirebaseDataGameToPlace(placeId: String, game: String): Completable
}