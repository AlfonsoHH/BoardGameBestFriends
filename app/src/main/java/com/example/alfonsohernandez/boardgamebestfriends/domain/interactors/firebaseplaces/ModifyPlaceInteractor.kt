package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import io.reactivex.Completable

interface ModifyPlaceInteractor {
    fun modifyFirebaseDataPlace(regionId: String, placeId: String, place: Place): Completable
}