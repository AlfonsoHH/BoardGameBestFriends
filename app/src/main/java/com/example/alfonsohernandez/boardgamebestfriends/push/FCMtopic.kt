package com.example.alfonsohernandez.boardgamebestfriends.push

import com.google.firebase.messaging.FirebaseMessaging

class FCMtopic {

    fun clearTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
    }


    fun setTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

}