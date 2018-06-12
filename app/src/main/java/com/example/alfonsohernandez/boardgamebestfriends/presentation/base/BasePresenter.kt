package com.example.alfonsohernandez.boardgamebestfriends.presentation.base

import android.app.Notification
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupContract
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler

abstract class BasePresenter<V : BaseView> {

    protected var view: V? = null

    abstract fun getUserProfile(): User?
    abstract fun firebaseEvent(id: String, activityName: String)

}