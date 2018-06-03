package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import io.reactivex.Completable

class AddMeetingToGroupInteractorImpl (private val meetingsRepository: MeetingsRepository): AddMeetingToGroupInteractor{
    override fun addFirebaseDataMeetingToGroup(groupId: String, meetingId: String): Completable {
        return Completable.create {
            try {
                meetingsRepository.addMeetingToGroup(groupId,meetingId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}