package com.example.alfonsohernandez.boardgamebestfriends.presentation.profile

import android.graphics.Bitmap
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView
import java.util.*

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface ProfileContract {

    interface View :
            //BaseNotificationView {
            BaseView {

        fun setData(userProfile: User?)
        fun setRegionData(region: Region)
        fun startPlaceDetail(id: String)
        fun startAddMyPlaceDialog()
        fun setPhotoImage(image: Any)

    }

    interface Presenter {

        fun cleanPaper()
        fun saveImage(data: Bitmap)
        fun getRealPathFromURI(contentUri: Uri): String
        fun myPlaceOnActualRegion()
        fun getRegionData(regionId: String)

    }

}