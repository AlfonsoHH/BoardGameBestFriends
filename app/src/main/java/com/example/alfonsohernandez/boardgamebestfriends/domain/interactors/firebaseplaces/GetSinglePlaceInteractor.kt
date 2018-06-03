package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetSinglePlaceInteractor {
    fun getFirebaseDataSinglePlace(regionId: String, placeId: String): Maybe<DataSnapshot>
}