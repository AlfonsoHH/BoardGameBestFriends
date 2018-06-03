package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetSingleMeetingInteractorImpl (private val meetingsRepository: MeetingsRepository): GetSingleMeetingInteractor{
    override fun getFirebaseDataSingleMeeting(regionId: String, meetingId: String): Maybe<DataSnapshot> {
        return meetingsRepository.getSingleMeetingRx(regionId,meetingId)
    }
}