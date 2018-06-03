package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.RegionsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetAllRegionInteractorImpl(private val regionsRepository: RegionsRepository): GetAllRegionInteractor {
    override fun getFirebaseDataAllRegions(): Maybe<DataSnapshot> {
        return regionsRepository.getAllRegionsRx()
    }
}