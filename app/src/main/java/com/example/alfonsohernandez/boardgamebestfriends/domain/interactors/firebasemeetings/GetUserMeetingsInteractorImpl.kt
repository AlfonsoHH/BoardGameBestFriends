package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetUserMeetingsInteractorImpl (private val meetingsRepository: MeetingsRepository): GetUserMeetingsInteractor{
    override fun getFirebaseDataUserMeetings(regionId: String, userId: String): Maybe<DataSnapshot> {
        return meetingsRepository.getUserMeetingsRx(regionId,userId)
    }
}