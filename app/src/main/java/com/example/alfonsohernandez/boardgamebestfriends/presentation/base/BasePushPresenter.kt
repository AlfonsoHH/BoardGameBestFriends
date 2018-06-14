package com.example.alfonsohernandez.boardgamebestfriends.presentation.base

import com.google.firebase.messaging.RemoteMessage.Notification
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.google.firebase.messaging.RemoteMessage

abstract class BasePushPresenter<V : BaseNotificationView>: FCMHandler.CallbackPush, BasePresenter<V>() {

    override fun pushReceived(rm: RemoteMessage) {

        rm?.let{
            view?.showNotification(it)
        }

    }
}