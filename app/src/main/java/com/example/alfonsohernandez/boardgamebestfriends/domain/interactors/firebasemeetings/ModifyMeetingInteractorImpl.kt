package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import io.reactivex.Completable

class ModifyMeetingInteractorImpl (private val meetingsRepository: MeetingsRepository): ModifyMeetingInteractor{
    override fun modifyFirebaseDataMeeting(regionId: String, meetingId: String, meeting: Meeting): Completable {
        return Completable.create {
            try {
                meetingsRepository.modifyMeeting(regionId,meetingId,meeting)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}