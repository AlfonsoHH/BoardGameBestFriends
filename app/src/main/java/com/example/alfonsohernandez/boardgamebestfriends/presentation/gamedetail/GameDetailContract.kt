package com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

interface GameDetailContract {

    interface View: BaseNotificationView {

        fun setData(game: Game)

    }

    interface Presenter {

        fun getGameData(gameId: String)
        fun firebaseEvent(id: String, activityName: String)

    }

}