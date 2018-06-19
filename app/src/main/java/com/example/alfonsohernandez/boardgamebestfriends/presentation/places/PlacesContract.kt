package com.example.alfonsohernandez.boardgamebestfriends.presentation.places

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView
import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface PlacesContract {

    interface View: BaseNotificationView {

        fun setData(places: ArrayList<Place>)

    }

    interface Presenter {

        fun getRegion(): LatLng?
        fun setPlacesData()
        fun getPlacesData(): ArrayList<Place>
        fun removePlace(place: Place)

    }

}