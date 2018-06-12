package com.example.alfonsohernandez.boardgamebestfriends.presentation.tab

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class TabContract {
    interface View: BaseView {

        fun setData()

        fun successLoadingRegions()
        fun successChangingRegion()

    }

    interface Presenter {

        fun getRegionList(): ArrayList<Region>
        fun getRegionId(cityName: String): String
        fun modifyUserInFirebaseDB(userId: String,user: User)
        fun saveUserInPaper(user: User)
        fun clearPaper()

    }
}