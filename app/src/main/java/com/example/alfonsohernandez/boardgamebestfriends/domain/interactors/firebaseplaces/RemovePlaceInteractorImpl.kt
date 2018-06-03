package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import io.reactivex.Completable

class RemovePlaceInteractorImpl(private val placesRepository: PlacesRepository): RemovePlaceInteractor {
    override fun removeFirebaseDatePlace(regionId: String, placeId: String): Completable {
        return Completable.create {
            try {
                placesRepository.removePlace(regionId,placeId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}