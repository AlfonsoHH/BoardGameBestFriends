package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetPlaceMeetingsInteractorImpl (private val meetingsRepository: MeetingsRepository): GetPlaceMeetingsInteractor{
    override fun getFirebaseDataPlaceMeetings(placeId: String): Maybe<DataSnapshot> {
        return meetingsRepository.getPlaceMeetingsRx(placeId)
    }
}