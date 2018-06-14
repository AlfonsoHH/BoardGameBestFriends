package com.example.alfonsohernandez.boardgamebestfriends.presentation.splash

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView
import com.google.firebase.auth.AuthCredential

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface SplashContract {

    interface View: BaseView {

        fun startAPP()
        fun startLogin()

    }

    interface Presenter {

        fun getAuthUser()

    }
}