package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import io.reactivex.Completable

class AddMeetingToUserInteractorImpl (private val meetingsRepository: MeetingsRepository): AddMeetingToUserInteractor{
    override fun addFirebaseDataMeetingToUser(regionId: String, userId: String, playing: Boolean, meetingId: String): Completable {
        return Completable.create {
            try {
                meetingsRepository.addMeetingToUser(regionId,userId,playing,meetingId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}