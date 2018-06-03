package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetOpenMeetingsInteractor {
    fun getFirebaseDataOpenMeetings(regionId: String): Maybe<DataSnapshot>
}