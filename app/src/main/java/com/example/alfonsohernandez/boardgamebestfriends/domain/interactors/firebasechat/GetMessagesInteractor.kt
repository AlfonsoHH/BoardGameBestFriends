package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat

import com.google.firebase.database.DataSnapshot
import io.reactivex.Flowable

interface GetMessagesInteractor {
    fun getFirebaseDataMessages(groupId: String): Flowable<DataSnapshot>
}