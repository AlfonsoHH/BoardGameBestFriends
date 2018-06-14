package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe
import io.reactivex.Observable

interface GetSingleUserInteractor {
    fun getFirebaseDataSingleUser(userId: String): Maybe<DataSnapshot>
}