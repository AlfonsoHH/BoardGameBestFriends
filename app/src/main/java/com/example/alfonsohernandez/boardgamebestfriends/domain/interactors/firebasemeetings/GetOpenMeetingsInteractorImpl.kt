package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetOpenMeetingsInteractorImpl (private val meetingsRepository: MeetingsRepository): GetOpenMeetingsInteractor{
    override fun getFirebaseDataOpenMeetings(regionId: String): Maybe<DataSnapshot> {
        return meetingsRepository.getOpenMeetingsRx(regionId)
    }
}