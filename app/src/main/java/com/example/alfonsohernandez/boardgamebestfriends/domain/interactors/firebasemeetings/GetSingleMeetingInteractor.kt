package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetSingleMeetingInteractor {
    fun getFirebaseDataSingleMeeting(regionId: String, meetingId: String): Maybe<DataSnapshot>
}