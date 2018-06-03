package com.example.alfonsohernandez.boardgamebestfriends.presentation.signup

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface SignUpContract {

    interface View {

        fun registerUser()
        fun setupSpinnerCountry(countryList: ArrayList<String>)
        fun setupSpinnerCity(cityList: ArrayList<String>)
        fun finishSignUp()

        fun showErrorRegister()
        fun showErrorEmail()
        fun showErrorPassword()
        fun showErrorUser()
        fun showErrorRegion()
        fun showErrorAllUsers()
        fun showErrorEmpty()

        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter {

        fun spinnerItemChange(countrySelected: String)

        fun saveUserData(userId:String, user: User)
        fun getUsersData(userName: String)
        fun getRegionData()

        fun getRegionIdFromCity(city: String): String

        fun getUrlFromPhoto(cursor: Cursor?): String
        fun getRealPathFromURI(context: Context, contentUri: Uri): String

        fun firebaseEvent(id: String, activityName: String)

    }

}