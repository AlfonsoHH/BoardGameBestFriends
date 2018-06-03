package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetUserPlacesInteractorImpl(private val placesRepository: PlacesRepository): GetUserPlacesInteractor {
    override fun getFirebaseDataUserPlaces(regionId: String, userId: String): Maybe<DataSnapshot> {
        return placesRepository.getUserPlacesRx(regionId,userId)
    }
}