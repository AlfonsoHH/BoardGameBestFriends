package com.example.alfonsohernandez.boardgamebestfriends.presentation.games

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

interface GamesContract {

    interface View {

        fun clearData()
        fun setSingleData(game: Game)
        fun setupRecycler()
        fun removeGame(game: Game)
        fun itemDetail(id: String)
        fun getSearchData():String

        fun showErrorLoading()
        fun showErrorBGG()
        fun showErrorRemoveDB()
        fun showErrorAddingToUser()
        fun showErrorAddingToGroup()
        fun showErrorAddingToPlace()
        fun showErrorRemovingToUser()
        fun showErrorRemovingToGroup()
        fun showErrorRemovingToPlace()

        fun successAddingToUser()
        fun successAddingToGroup()
        fun successAddingToPlace()
        fun successRemovingToUser()
        fun successRemovingToGroup()
        fun successRemovingToPlace()

        fun showProgressBar(isLoading: Boolean)
        fun noResult()

    }

    interface Presenter {

        fun getUserProfile(): User?

        fun getGamesData()
        fun getUserGamesData()
        fun getGroupGamesData()
        fun getPlaceGamesData()
        fun getSingleGameData(gameId: String)

        fun dataChooser()

        fun addRemoveItem(adding: Boolean, itemId: String, kind: String)

        fun getBGGdata(bggId: String)
        fun loadAllGamesDB(bddId: String)
        fun gameExistDB(game: Game)
        fun addGameBgg(exist: Boolean, game: Game)
        fun addGameToUserBgg(game: Game)

        fun firebaseEvent(id: String, activityName: String)

    }

}