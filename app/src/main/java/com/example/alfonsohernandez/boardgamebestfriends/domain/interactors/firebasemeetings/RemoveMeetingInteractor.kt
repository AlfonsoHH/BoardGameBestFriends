package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import io.reactivex.Completable

interface RemoveMeetingInteractor {
    fun removeFirebaseDataMeeting(regionId: String, meetingId: String): Completable
}