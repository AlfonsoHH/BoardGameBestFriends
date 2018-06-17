package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetPlacesInteractorImpl(private val placesRepository: PlacesRepository):GetPlacesInteractor {
    override fun getFirebaseDataOpenPlaces(regionId: String): Maybe<DataSnapshot> {
        return placesRepository.getPlacesRx(regionId)
    }
}