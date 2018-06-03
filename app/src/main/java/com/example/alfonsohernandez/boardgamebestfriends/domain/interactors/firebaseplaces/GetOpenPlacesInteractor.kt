package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetOpenPlacesInteractor {
    fun getFirebaseDataOpenPlaces(regionId: String): Maybe<DataSnapshot>
}