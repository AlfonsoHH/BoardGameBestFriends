package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetSinglePlaceInteractorImpl(private val placesRepository: PlacesRepository): GetSinglePlaceInteractor {
    override fun getFirebaseDataSinglePlace(regionId: String, placeId: String): Maybe<DataSnapshot> {
        return placesRepository.getSinglePlaceRx(regionId,placeId)
    }
}