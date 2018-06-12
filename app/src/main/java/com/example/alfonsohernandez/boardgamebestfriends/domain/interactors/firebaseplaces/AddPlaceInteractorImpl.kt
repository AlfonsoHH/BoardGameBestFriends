package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import io.reactivex.Completable

class AddPlaceInteractorImpl(private val placesRepository: PlacesRepository): AddPlaceInteractor {
    override fun addFirebaseDataPlace(key: String, regionId: String, place: Place): Completable {
        return Completable.create {
            try {
                placesRepository.addPlace(key,regionId,place)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
    override fun getKey(): String {
        return placesRepository.getKey()
    }
}