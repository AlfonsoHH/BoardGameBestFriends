package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Flowable

class ChatRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun sendMessage(groupId: String, message: Message){
        val key = firebaseInstance.push().key
        firebaseInstance.child("groups").child(groupId).child("chat").child(key).setValue(message)
    }

    fun getMessagesRx(groupId: String): Flowable<DataSnapshot> {
        return RxFirebaseDatabase.observeValueEvent(firebaseInstance.child("groups").child(groupId).child("chat").orderByKey())
    }
}