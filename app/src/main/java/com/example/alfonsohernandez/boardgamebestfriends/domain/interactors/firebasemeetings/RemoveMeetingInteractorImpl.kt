package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import io.reactivex.Completable

class RemoveMeetingInteractorImpl (private val meetingsRepository: MeetingsRepository): RemoveMeetingInteractor{
    override fun removeFirebaseDataMeeting(regionId: String, meetingId: String): Completable {
        return Completable.create {
            try {
                meetingsRepository.removeMeeting(regionId,meetingId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}