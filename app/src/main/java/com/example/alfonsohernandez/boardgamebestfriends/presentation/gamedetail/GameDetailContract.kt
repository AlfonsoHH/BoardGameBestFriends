package com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

interface GameDetailContract {

    interface View {

        fun setData(game: Game)
        fun showErrorLoading()
        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter {

        fun getGameData(gameId: String)
        fun firebaseEvent(id: String, activityName: String)

    }

}