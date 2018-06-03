package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetRegionInteractor {
    fun getFirebaseDataSingleRegion(regionId: String): Maybe<DataSnapshot>
}