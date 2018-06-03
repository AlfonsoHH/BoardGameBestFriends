package com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Rule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

interface PlaceDetailContract {

    interface View {

        fun setData(place: Place, region: String)
        fun setRuleData(ruleId: Int, ruleNumber: Int)

        fun showErrorPlaces()
        fun showErrorRules()
        fun showErrorRegion()

        fun successModify()

        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter {

        fun getUserProfile(): User?

        fun getRegionData(regionId: String, place: Place)
        fun getPlaceData(regionId: String, placeId: String)

        fun firebaseEvent(id: String, activityName: String)

    }

}