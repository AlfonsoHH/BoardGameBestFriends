package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.MeetingsRepository
import com.google.firebase.database.DataSnapshot
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

class GetGroupMeetingsInteractorImpl (private val meetingsRepository: MeetingsRepository): GetGroupMeetingsInteractor{
    override fun getFirebaseDataGroupMeetings(groupId: String): Maybe<DataSnapshot> {
        return meetingsRepository.getGroupMeetingsRx(groupId)
    }
}