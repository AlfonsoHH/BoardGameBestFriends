package com.example.alfonsohernandez.boardgamebestfriends.presentation.profile

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import java.util.*

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface ProfileContract {

    interface View {

        fun setData(userProfile: User?)
        fun setRegionData(region: Region)
        fun startPlaceDetail(id: String)
        fun startAddMyPlaceDialog()

        fun showErrorLogout()
        fun showErrorRegion()
        fun showErrorPlaces()

        fun successAddingMyPlace()

        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter {

        fun getUserProfile(): User?

        fun getUserPlaces()
        fun getSinglePlace(placeId: String)
        fun myPlaceOnActualRegion()
        fun getRegionData(regionId: String)

        fun firebaseEvent(id: String, activityName: String)

    }

}