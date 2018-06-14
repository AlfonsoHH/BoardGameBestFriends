package com.example.alfonsohernandez.boardgamebestfriends.presentation.base

import com.google.firebase.messaging.RemoteMessage

interface BaseNotificationView : BaseView {

    fun showNotification(rm: RemoteMessage)

}