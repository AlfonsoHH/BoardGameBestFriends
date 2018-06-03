package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetSingleGroupInteractor {
    fun getFirebaseDataSingleGroup(groupId: String): Maybe<DataSnapshot>
}