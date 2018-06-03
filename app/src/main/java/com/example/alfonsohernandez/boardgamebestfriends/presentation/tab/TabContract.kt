package com.example.alfonsohernandez.boardgamebestfriends.presentation.tab

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class TabContract {
    interface View {

        fun setData()

        fun showErrorLoadingRegions()
        fun showErrorSavingUser()

        fun successLoadingRegions()
        fun successChangingRegion()

    }

    interface Presenter {

        fun getUserProfile(): User?

        fun loadRegions()
        fun getCountryList(): ArrayList<String>
        fun getCityList(countryName: String): ArrayList<String>
        fun getRegionId(cityName: String): String

        fun modifyUserInFirebaseDB(userId: String,user: User)
        fun saveUserInPaper(user: User)

        fun firebaseEvent(id: String, activityName: String)

    }
}