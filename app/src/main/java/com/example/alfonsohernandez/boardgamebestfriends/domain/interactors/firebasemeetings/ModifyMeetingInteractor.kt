package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import io.reactivex.Completable

interface ModifyMeetingInteractor {
    fun modifyFirebaseDataMeeting(regionId: String, meetingId: String, meeting: Meeting): Completable
}