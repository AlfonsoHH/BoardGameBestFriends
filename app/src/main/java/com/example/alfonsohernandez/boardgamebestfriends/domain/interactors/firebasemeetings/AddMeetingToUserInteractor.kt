package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import io.reactivex.Completable

interface AddMeetingToUserInteractor {
    fun addFirebaseDataMeetingToUser(regionId: String, userId: String, playing: Boolean, meetingId: String): Completable
}