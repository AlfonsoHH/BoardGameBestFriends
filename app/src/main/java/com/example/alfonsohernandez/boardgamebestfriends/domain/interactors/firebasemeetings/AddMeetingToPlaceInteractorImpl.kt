package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import io.reactivex.Completable

class AddMeetingToPlaceInteractorImpl (private val meetingsRepository: MeetingsRepository): AddMeetingToPlaceInteractor{
    override fun addFirebaseDataMeetingToPlace(placeId: String, meetingId: String): Completable {
        return Completable.create {
            try {
                meetingsRepository.addMeetingToPlace(placeId,meetingId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}