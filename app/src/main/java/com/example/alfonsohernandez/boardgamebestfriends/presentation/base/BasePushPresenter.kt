package com.example.alfonsohernandez.boardgamebestfriends.presentation.base

import com.google.firebase.messaging.RemoteMessage.Notification
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler

abstract class BasePushPresenter<V : BaseNotificationView>: FCMHandler.CallbackPush, BasePresenter<V>() {

    override fun pushReceived(not: Notification) {
        not.title?.let{
            view?.showNotification(it)
        }
    }
}