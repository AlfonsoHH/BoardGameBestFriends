package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import io.reactivex.Completable

interface AddMeetingToGroupInteractor {
    fun addFirebaseDataMeetingToGroup(groupId: String, meetingId: String): Completable
}