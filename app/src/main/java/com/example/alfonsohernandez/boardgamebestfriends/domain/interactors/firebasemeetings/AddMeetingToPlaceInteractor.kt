package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import io.reactivex.Completable

interface AddMeetingToPlaceInteractor {
    fun addFirebaseDataMeetingToPlace(placeId: String, meetingId: String): Completable
}