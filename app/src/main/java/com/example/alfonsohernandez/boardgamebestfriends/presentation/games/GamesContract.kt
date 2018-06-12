package com.example.alfonsohernandez.boardgamebestfriends.presentation.games

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

interface GamesContract {

    interface View: BaseView{

        fun setData(games: ArrayList<Game>)
        fun setupRecycler()
        fun removeGame(game: Game)
        fun itemDetail(id: String)
        fun getSearchData():String

        fun showProgressDialog(isLoading: Boolean)

    }

    interface Presenter {

        fun getGamesData()
        fun getUserGamesData()
        fun getGroupGamesData()
        fun getPlaceGamesData()

        fun dataChooser()

        fun addRemoveItem(adding: Boolean, itemId: String, kind: String)

        fun getBGGdata(bggId: String)
        fun gameExistDB(game: Game)
        fun addGameBgg(exist: Boolean, game: Game)
        fun addGameToUserBgg(game: Game)

    }

}