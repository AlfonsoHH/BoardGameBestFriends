package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import io.reactivex.Completable

class AddMeetingInteractorImpl (private val meetingsRepository: MeetingsRepository): AddMeetingInteractor {
    override fun addFirebaseDataMeeting(regionId: String, meeting: Meeting): Completable {
        return Completable.create {
            try {
                meetingsRepository.addMeeting(regionId,meeting)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}