package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetOpenPlacesInteractorImpl(private val placesRepository: PlacesRepository):GetOpenPlacesInteractor {
    override fun getFirebaseDataOpenPlaces(regionId: String): Maybe<DataSnapshot> {
        return placesRepository.getOpenPlacesRx(regionId)
    }
}