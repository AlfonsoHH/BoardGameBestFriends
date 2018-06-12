package com.example.alfonsohernandez.boardgamebestfriends.presentation.signup

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface SignUpContract {

    interface View: BaseView {

        fun setupSpinnerCountry(countryList: ArrayList<String>)
        fun setupSpinnerCity(cityList: ArrayList<String>)
        fun finishSignUp()
        fun setPhotoImage(image: Any)
        fun saveUser(user: FirebaseUser)

    }

    interface Presenter {

        fun spinnerItemChange(countrySelected: String)
        fun saveUserData(userId:String, user: User)
        fun saveImage(name: String,user: User,data: Bitmap)
        fun getUsersData(userMail: String, password: String)
        fun getRegionData()
        fun getRegionIdFromCity(city: String): String
        fun getRealPathFromURI(contentUri: Uri): String

    }

}