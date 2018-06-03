package com.example.alfonsohernandez.boardgamebestfriends.presentation.games

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GamesPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                         private val getAllGamesInteractor: GetAllGamesInteractor,
                                         private val getUserGamesInteractor: GetUserGamesInteractor,
                                         private val getGroupGamesInteractor: GetGroupGamesInteractor,
                                         private val getPlaceGamesInteractor: GetPlaceGamesInteractor,
                                         private val getSingleGameInteractor: GetSingleGameInteractor,
                                         private val getBggGameCollectionInteractor: GetBggGameCollectionInteractor,
                                         private val addGameInteractor: AddGameInteractor,
                                         private val addGameToUserInteractor: AddGameToUserInteractor,
                                         private val addGameToGroupInteractor: AddGameToGroupInteractor,
                                         private val addGameToPlaceInteractor: AddGameToPlaceInteractor,
                                         private val removeGameToUserInteractor: RemoveGameToUserInteractor,
                                         private val removeGameToGroupInteractor: RemoveGameToGroupInteractor,
                                         private val removeGameToPlaceInteractor: RemoveGameToPlaceInteractor,
                                         private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GamesContract.Presenter {

    private var view: GamesContract.View? = null

    private val TAG = "GamesPresenter"

    var kind = ""
    var search = ""
    var gameList = arrayListOf<Game>()

    fun setView(view: GamesContract.View?, kind: String) {
        this.view = view
        this.kind = kind
        firebaseEvent("Showing games",TAG)
        dataChooser()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getBGGdata(bggId: String) {
        view?.showProgressBar(true)
        getBggGameCollectionInteractor
                .getGameCollection(bggId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    for (game in it){
                        if(game.owned!! && !game.isExpansion!!) {
                            gameExistDB(Game(game.gameId.toString(),
                                    game.thumbnail.toString(),
                                    game.name.toString(),
                                    game.userComment.toString(),
                                    game.minPlayers!!.toInt(),
                                    game.maxPlayers!!.toInt(),
                                    game.playingTime!!.toInt(),
                                    game.averageRating!!.toDouble()))
                        }
                    }
                    view?.showProgressBar(false)
                }, {
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun loadAllGamesDB(bggId: String) {
        view?.showProgressBar(true)
        getAllGamesInteractor
                .getFirebaseDataAllGames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    gameList.clear()
                    for (h in it.children) {
                        gameList.add(h.getValue(Game::class.java)!!)
                    }
                    getBGGdata(bggId)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun gameExistDB(game: Game) {
        var exist = false
        for(gameDB in gameList){
            if(gameDB.id.equals(game.id))
                exist = true
        }
        addGameBgg(exist, game)
    }

    override fun addGameBgg(exist: Boolean, game: Game) {
        if(exist)
            addGameToUserBgg(game)
        else
            addGameInteractor
                    .AddFirebaseDataGame(game)
                    .subscribe({
                        Log.d(TAG,"Game Added to DB: "+game.title)
                        addGameToUserBgg(game)
                    },{
                        Log.d(TAG,"Error adding game to DB: "+game.title)
                    })
    }

    override fun addGameToUserBgg(game: Game) {
        addGameToUserInteractor
                .addFirebaseDataGameToUser(getUserProfile()!!.id, game.id)
                .subscribe({
                    Log.d(TAG,"Game Added to user: "+game.title)
                },{
                    Log.d(TAG,"Error adding game to user: "+game.title)
                })
    }

    override fun dataChooser() {
        if(view != null) {
            search = view?.getSearchData()!!
            if (kind!!.contains("buddy-"))
                getUserGamesData()
            else if (kind!!.contains("group-"))
                getGroupGamesData()
            else if (kind!!.contains("place-"))
                getPlaceGamesData()
            else
                getGamesData()
        }
    }

    override fun getGamesData() {
        view?.showProgressBar(true)
        getAllGamesInteractor
                .getFirebaseDataAllGames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        view?.setSingleData(h.getValue(Game::class.java)!!)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getUserGamesData() {
        view?.showProgressBar(true)
        Log.d(TAG,kind.substring(5, kind.length - 1))
        getUserGamesInteractor
                .getFirebaseDataUserGames(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.clearData()
                    for (h in it.children) {
                        getSingleGameData(h.key)
                    }
                    view?.showProgressBar(false)
                }, {
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getGroupGamesData() {
        view?.showProgressBar(true)
        getGroupGamesInteractor
                .getFirebaseDataGroupGames(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.clearData()
                    for (h in it.children) {
                        getSingleGameData(h.key)
                    }
                    view?.showProgressBar(false)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getPlaceGamesData() {
        view?.showProgressBar(true)
        Log.d(TAG,kind.substring(6, kind.length))
        getPlaceGamesInteractor
                .getFirebaseDataPlaceGames(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.clearData()
                    for (h in it.children) {
                        getSingleGameData(h.key)
                    }
                    view?.showProgressBar(false)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getSingleGameData(gameId: String) {
        getSingleGameInteractor
                .getFirebaseDataSingleGame(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    var actualGame = it.getValue(Game::class.java)
                    if(search.equals(""))
                        view?.setSingleData(actualGame!!)
                    else
                        if (actualGame!!.title.contains(search))
                            view?.setSingleData(actualGame!!)
                },{
                    view?.showErrorLoading()
                })
    }

    override fun addRemoveItem(adding: Boolean, itemId: String, kind: String) {
        var id = kind.substring(6,kind.length)
        if(adding) {
            if (kind.contains("buddy-"))
                addGameToUserInteractor
                        .addFirebaseDataGameToUser(id, itemId)
                        .subscribe({
                            view?.successAddingToUser()
                        },{
                            view?.showErrorAddingToUser()
                        })
            else if (kind.contains("group-"))
                addGameToGroupInteractor
                        .AddFirebaseDataGameToGroup(id, itemId)
                        .subscribe({
                            view?.successAddingToGroup()
                        },{
                            view?.showErrorAddingToUser()
                        })
            else if (kind.contains("place-"))
                addGameToPlaceInteractor
                        .AddFirebaseDataGameToPlace(id, itemId)
                        .subscribe({
                            view?.successAddingToPlace()
                        },{
                            view?.showErrorAddingToGroup()
                        })
            else
                view?.showErrorRemoveDB()
        }else{
            if (kind.contains("buddy-"))
                removeGameToUserInteractor
                        .removeFirebaseDataGameToUser(id, itemId)
                        .subscribe({
                            view?.successRemovingToUser()
                        },{
                            view?.showErrorRemovingToUser()
                        })
            else if (kind.contains("group-"))
                removeGameToGroupInteractor
                        .removeFirebaseDataGameToGroup(id, itemId)
                        .subscribe({
                            view?.successRemovingToGroup()
                        },{
                            view?.showErrorRemovingToGroup()
                        })
            else if (kind.contains("place-"))
                removeGameToPlaceInteractor
                        .RemoveFirebaseDataGameToPlace(id, itemId)
                        .subscribe({
                            view?.successRemovingToPlace()
                        },{
                            view?.showErrorRemovingToPlace()
                        })
            else
                view?.showErrorRemoveDB()
        }
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }
}