package com.example.alfonsohernandez.boardgamebestfriends.presentation.login

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface LoginContract {

    interface View {

        fun nextActivity()

        fun showErrorChecking()
        fun showErrorEmail()
        fun showErrorFacebook()
        fun showErrorGmail()
        fun showErrorSavingUser()
        fun showErrorLoadingUsers()
        fun showErrorLoadingRegions()

        fun successAdding()

        fun showProgressBar(isLoading: Boolean)

    }

    interface Presenter {

        fun getUserProfile():User?
        fun getUsersData(userId: String,user: User)

        fun loadRegions()
        fun getCountryList(): ArrayList<String>
        fun getCityList(countryName: String): ArrayList<String>
        fun getRegionId(cityName: String): String

        fun getSingleUser(userId: String)
        fun saveUserInFirebaseDB(userId: String, user: User)
        fun modifyUserInFirebaseDB(userId: String,user: User)
        fun saveUserInPaper(user: User)
        fun firebaseEvent(id:String,activityName:String)

    }

}