package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PlacesRepository
import io.reactivex.Completable

class ModifyPlaceInteractorImpl(private val placesRepository: PlacesRepository): ModifyPlaceInteractor {
    override fun modifyFirebaseDataPlace(regionId: String, placeId: String, place: Place): Completable {
        return Completable.create {
            try {
                placesRepository.modifyPlace(regionId,placeId,place)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}