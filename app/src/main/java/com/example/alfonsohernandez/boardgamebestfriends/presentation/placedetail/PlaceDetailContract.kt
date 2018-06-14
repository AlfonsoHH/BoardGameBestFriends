package com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

interface PlaceDetailContract {

    interface View: BaseNotificationView {

        fun setData(place: Place, region: String)
        fun setRuleData(ruleId: Int, ruleNumber: Int)

    }

    interface Presenter {

        fun getDay(day: String): String
        fun getHours(hours: String): String

    }

}