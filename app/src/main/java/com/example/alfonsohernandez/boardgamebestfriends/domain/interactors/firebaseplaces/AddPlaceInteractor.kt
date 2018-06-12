package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import io.reactivex.Completable

interface AddPlaceInteractor {
    fun addFirebaseDataPlace(key: String, regionId: String, place: Place): Completable
    fun getKey():String
}