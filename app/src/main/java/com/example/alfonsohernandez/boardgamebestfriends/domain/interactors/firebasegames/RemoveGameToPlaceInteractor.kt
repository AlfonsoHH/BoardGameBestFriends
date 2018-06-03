package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import io.reactivex.Completable

interface RemoveGameToPlaceInteractor {
    fun RemoveFirebaseDataGameToPlace(placeId: String, gameId: String): Completable
}