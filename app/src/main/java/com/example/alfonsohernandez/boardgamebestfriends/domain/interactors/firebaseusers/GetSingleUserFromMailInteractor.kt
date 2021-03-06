package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

interface GetSingleUserFromMailInteractor {
    fun getFirebaseDataSingleUserFromMail(email: String): Maybe<DataSnapshot>
}