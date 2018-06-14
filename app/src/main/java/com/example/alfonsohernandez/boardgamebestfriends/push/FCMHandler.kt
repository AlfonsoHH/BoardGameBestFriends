package com.example.alfonsohernandez.boardgamebestfriends.push

import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessage.Notification

class FCMHandler {

    var push: CallbackPush? = null

    interface CallbackPush{
        fun pushReceived(rm: RemoteMessage)
    }

    fun handlePush(rm: RemoteMessage) {
        push?.pushReceived(rm)
    }

}