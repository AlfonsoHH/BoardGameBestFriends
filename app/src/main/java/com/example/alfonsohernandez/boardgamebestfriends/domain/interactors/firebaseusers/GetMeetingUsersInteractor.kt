package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetMeetingUsersInteractor {
    fun getFirebaseDataMeetingUsers(regionId: String, meetingId: String): Maybe<DataSnapshot>
}