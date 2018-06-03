package com.example.alfonsohernandez.boardgamebestfriends.presentation.places

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import java.util.ArrayList

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface PlacesContract {

    interface View {

        fun setData(places: ArrayList<Place>)

        fun showErrorPlaces()
        fun showErrorRegion()

        fun successAdding()

        fun showProgress(isLoading:Boolean)

    }

    interface Presenter {

        fun getUserProfile(): User?
        fun getPlacesData()

    }

}