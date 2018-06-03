package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetUserGroupsInteractor {
    fun getFirebaseDataUserGroups(userId: String): Maybe<DataSnapshot>
}