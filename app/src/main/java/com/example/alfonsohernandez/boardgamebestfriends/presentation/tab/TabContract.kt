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

    }

    interface Presenter {}
}