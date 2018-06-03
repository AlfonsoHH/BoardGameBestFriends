package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import io.reactivex.Completable

interface RemoveMeetingToUserInteractor {
    fun removeFirebaseDataMeeting(regionId: String, userId: String, meetingId: String): Completable
}