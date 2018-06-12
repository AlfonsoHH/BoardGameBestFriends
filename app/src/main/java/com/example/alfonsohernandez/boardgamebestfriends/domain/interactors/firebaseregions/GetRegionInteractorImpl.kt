package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.RegionsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetRegionInteractorImpl(private val regionsRepository: RegionsRepository): GetRegionInteractor {
    override fun getFirebaseDataSingleRegion(name: String): Maybe<DataSnapshot> {
        return regionsRepository.getRegionRx(name)
    }
}