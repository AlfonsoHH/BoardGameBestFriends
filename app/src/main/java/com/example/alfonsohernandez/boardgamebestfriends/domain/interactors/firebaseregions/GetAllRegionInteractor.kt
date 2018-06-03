package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetAllRegionInteractor {
    fun getFirebaseDataAllRegions(): Maybe<DataSnapshot>
}