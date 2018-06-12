package com.example.alfonsohernandez.boardgamebestfriends.push

import com.google.firebase.messaging.RemoteMessage.Notification

class FCMHandler {

    var push: CallbackPush? = null

    interface CallbackPush{
        fun pushReceived(not: Notification)
    }

    fun handlePush(not: Notification) {
        push?.pushReceived(not)
    }

}