package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import io.reactivex.Completable

interface AddMeetingInteractor {
    fun addFirebaseDataMeeting(regionId: String, meeting: Meeting): Completable
}