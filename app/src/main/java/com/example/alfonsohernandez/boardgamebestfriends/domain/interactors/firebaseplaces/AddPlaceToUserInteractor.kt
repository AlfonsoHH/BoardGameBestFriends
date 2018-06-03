package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import io.reactivex.Completable

interface AddPlaceToUserInteractor {
    fun addFirebaseDataPlaceToUser(regionId: String, userId: String, placeId: String): Completable
}