package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetUserPlacesInteractor {
    fun getFirebaseDataUserPlaces(regionId: String, userId: String): Maybe<DataSnapshot>
}