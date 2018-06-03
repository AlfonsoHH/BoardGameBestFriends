package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import io.reactivex.Completable

class AddPlaceToUserInteractorImpl (private val placesRepository: PlacesRepository): AddPlaceToUserInteractor {
    override fun addFirebaseDataPlaceToUser(regionId: String, userId: String, placeId: String): Completable {
        return Completable.create {
            try {
                placesRepository.addPlaceToUser(regionId,userId,placeId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}