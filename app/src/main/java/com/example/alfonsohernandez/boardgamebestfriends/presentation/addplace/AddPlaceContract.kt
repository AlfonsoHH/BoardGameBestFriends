package com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Rule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.google.android.gms.maps.model.LatLng

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface AddPlaceContract {

    interface View{

        fun setData(place: Place)
        fun setSpinnerData()
        fun finishAddPlace()
        fun getDayString(day: Int): String

        fun showErrorAdding()
        fun showErrorCity()
        fun showErrorUnknown()
        fun showErrorRules()
        fun showErrorAddingPlace()
        fun showErrorAddingPlaceToUser()
        fun showErrorModify()
        fun showErrorEmpty()
        fun showErrorRegion()
        fun showErrorAddress()

        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter{

        fun getUserProfile(): User?

        fun savePlace(context: Context, place: Place)
        fun modifyPlace(context: Context, place: Place)

        fun generateDayList(mon: Boolean,
                            tue: Boolean,
                            wed: Boolean,
                            thu: Boolean,
                            fri: Boolean,
                            sat: Boolean,
                            sun: Boolean): ArrayList<Boolean>
        fun convertDays(days: ArrayList<Boolean>): String
        fun convertHours(openMor: String,
                         closeMor: String,
                         aftDif: Boolean,
                         openAft: String,
                         closeAft: String): String
        fun getLatLongFromAddress(context: Context, address: String): LatLng?
        fun getUrlFromPhoto(cursor: Cursor?): String
        fun getRealPathFromURI(context: Context, contentUri: Uri): String

        fun getPlace(placeId: String)
        fun getRegion()

        fun firebaseEvent(id: String, activityName: String)
    }

}