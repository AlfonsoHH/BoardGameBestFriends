package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetPlaceMeetingsInteractor {
    fun getFirebaseDataPlaceMeetings(placeId: String): Maybe<DataSnapshot>
}