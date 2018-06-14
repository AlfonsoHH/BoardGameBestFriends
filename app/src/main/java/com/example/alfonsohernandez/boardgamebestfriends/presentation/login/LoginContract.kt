package com.example.alfonsohernandez.boardgamebestfriends.presentation.login

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface LoginContract {

    interface View: BaseView {

        fun nextActivity()
        fun chooseRegion(fromFacebook: Boolean)

    }

    interface Presenter {

        fun getUsersData(userId: String,user: User)
        fun getAuthUser(): FirebaseUser?

        fun loginWithEmail(email: String, password: String)
        fun loginWithCredentials(credential: AuthCredential, fromFacebook: Boolean)

        fun loadRegions()
        fun getRegions(): ArrayList<Region>
        fun getRegionId(cityName: String): String

        fun getSingleUser(userId: String)
        fun saveUserInFirebaseDB(userId: String, user: User)
        fun modifyUserInFirebaseDB(userId: String,user: User)
        fun saveUserInPaper(user: User)

    }

}