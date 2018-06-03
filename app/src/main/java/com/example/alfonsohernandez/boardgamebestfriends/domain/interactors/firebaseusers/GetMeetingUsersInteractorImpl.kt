package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.UsersRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetMeetingUsersInteractorImpl (private val usersRepository: UsersRepository): GetMeetingUsersInteractor {
    override fun getFirebaseDataMeetingUsers(regionId: String, meetingId: String): Maybe<DataSnapshot> {
        return usersRepository.getMeetingUsersRX(regionId, meetingId)
    }
}