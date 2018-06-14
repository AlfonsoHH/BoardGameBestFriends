package com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Rule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView
import com.google.android.gms.maps.model.LatLng

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface AddPlaceContract {

    interface View: BaseNotificationView {

        fun setData(place: Place)
        fun setSpinnerData()
        fun finishAddPlace()
        fun setPhotoImage(image: Any)

    }

    interface Presenter{

        fun saveImage(place: Place, data: Bitmap, modifiedPhoto: Boolean, actionModify: Boolean)
        fun savePlace(key: String, place: Place)
        fun modifyPlace(place: Place)
        fun getLatLongFromAddress(address: String): LatLng?
        fun getRealPathFromURI(contentUri: Uri): String

        fun getPlace(placeId: String)

    }

}