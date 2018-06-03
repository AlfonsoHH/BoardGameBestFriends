package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetGroupUsersInteractor {
    fun getFirebaseDataGroupUsers(groupId: String): Maybe<DataSnapshot>
}