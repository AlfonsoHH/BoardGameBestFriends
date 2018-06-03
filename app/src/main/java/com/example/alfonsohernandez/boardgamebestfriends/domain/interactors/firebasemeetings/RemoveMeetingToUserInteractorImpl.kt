package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import io.reactivex.Completable

class RemoveMeetingToUserInteractorImpl(private val meetingsRepository: MeetingsRepository): RemoveMeetingToUserInteractor {
    override fun removeFirebaseDataMeeting(regionId: String, userId: String, meetingId: String): Completable {
        return Completable.create {
            try {
                meetingsRepository.removeMeetingToUser(regionId,userId, meetingId)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}