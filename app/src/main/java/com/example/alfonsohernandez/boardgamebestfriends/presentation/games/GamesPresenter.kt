package com.example.alfonsohernandez.boardgamebestfriends.presentation.games

import android.os.Handler
import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggXmlGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamedetail.GetBggGameDetailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.lang.Thread.sleep
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GamesPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                         private val paperGamesInteractor: PaperGamesInteractor,
                                         private val getUserGamesInteractor: GetUserGamesInteractor,
                                         private val getGroupGamesInteractor: GetGroupGamesInteractor,
                                         private val getPlaceGamesInteractor: GetPlaceGamesInteractor,
                                         private val getBggXmlGameCollectionInteractor: GetBggXmlGameCollectionInteractor,
                                         private val getBggGameDetailInteractor: GetBggGameDetailInteractor,
                                         private val addGameInteractor: AddGameInteractor,
                                         private val addGameToUserInteractor: AddGameToUserInteractor,
                                         private val addGameToGroupInteractor: AddGameToGroupInteractor,
                                         private val addGameToPlaceInteractor: AddGameToPlaceInteractor,
                                         private val removeGameToUserInteractor: RemoveGameToUserInteractor,
                                         private val removeGameToGroupInteractor: RemoveGameToGroupInteractor,
                                         private val removeGameToPlaceInteractor: RemoveGameToPlaceInteractor,
                                         private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GamesContract.Presenter, BasePresenter<GamesContract.View>(){

    private val TAG = "GamesPresenter"

    var kind = ""
    var search = ""

    private var mTimer1: Timer? = null
    private var mTt1: TimerTask? = null
    private val mTimerHandler = Handler()

    private fun stopTimer() {
        if (mTimer1 != null) {
            mTimer1!!.cancel()
            mTimer1!!.purge()
            view?.showProgressDialog(false)
        }
    }

    private fun startTimer(listGameDetails: ArrayList<String>) {
        mTimer1 = Timer()
        var counter = 0
        mTt1 = object : TimerTask() {
            override fun run() {
                mTimerHandler.post {
                    if(counter<=listGameDetails.size-1) {
                        getGameDetail(listGameDetails.get(counter))
                        counter = counter + 1
                    }else{
                        stopTimer()
                    }
                }
            }
        }
        mTimer1!!.schedule(mTt1, 1, 5000)
    }

    fun setView(view: GamesContract.View?, kind: String) {
        this.view = view
        this.kind = kind
        firebaseEvent("Showing games",TAG)
        dataChooser()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun getBGGdata(bggId: String) {
        view?.showProgress(true)
        view?.showProgressDialog(true)
        getBggXmlGameCollectionInteractor
                .getGameCollectionXML(bggId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.item != null) {
                        val listGameDetails = arrayListOf<String>()
                        it.item?.let { games ->
                            for (game in games) {
                                if (paperGamesInteractor.get(game?.objectid.toString()) != null)
                                    addGameBgg(true, paperGamesInteractor.get(game?.objectid.toString())!!)
                                else
                                    listGameDetails.add(game?.objectid.toString())
                            }
                            startTimer(listGameDetails)
                        }
                        view?.showProgress(false)
                    }
                }, {
                    view?.showProgressDialog(false)
                    view?.showProgress(false)
                    view?.showError(R.string.gamesErrorBGG)
                })
    }

    fun getGameDetail(gameId: String){
        getBggGameDetailInteractor
                .getGameDetail(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    it.item?.let { game ->
                        addGameBgg(false, Game(gameId,
                                game.thumbnail!!,
                                game.name!!.get(0)!!.value.toString(),
                                game.description.toString(),
                                game.minplayers!!.value!!.toInt(),
                                game.maxplayers!!.value!!.toInt(),
                                game.playingtime!!.value!!.toInt(),
                                game.statistics!!.ratings!!.average!!.value!!.toDouble()))
                    }
                }, {
                    view?.showError(R.string.gameDetailErrorLoading)
                })
    }

    override fun gameExistDB(game: Game) {
        var exist = false
        for(gameDB in paperGamesInteractor.all()){
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
                        paperGamesInteractor.add(game)
                        Timber.d(TAG + " Game Added to DB: %s",game.title)
                        addGameToUserBgg(game)
                    },{
                        Timber.d(TAG + " Error adding game to DB: %s",game.title)
                    })
    }

    override fun addGameToUserBgg(game: Game) {
        getUserProfile()?.let {
            addGameToUserInteractor
                    .addFirebaseDataGameToUser(it.id, game.id)
                    .subscribe({
                        Timber.d(TAG + " Game Added to user:  %s", game.title)
                    }, {
                        Timber.d(TAG + " Error adding game to user: %s", game.title)
                    })
        }
    }

    override fun dataChooser() {
        view?.getSearchData()?.let{
            search = it
        }
        if (kind.contains("buddy-"))
            getUserGamesData()
        else if (kind.contains("group-"))
            getGroupGamesData()
        else if (kind.contains("place-"))
            getPlaceGamesData()
        else
            getGamesData()
    }

    override fun getGamesData() {
        if (search.equals(""))
            view?.setData(paperGamesInteractor.all())
        else
            view?.setData(paperGamesInteractor.all().filter { it.title.contains(search) } as ArrayList<Game>)
    }

    override fun getUserGamesData() {
        view?.showProgress(true)
        getUserGamesInteractor
                .getFirebaseDataUserGames(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    var listAdapter = arrayListOf<Game>()
                    for (h in it.children) {
                        if (search.equals("")) {
                            paperGamesInteractor.get(h.key)?.let{
                                listAdapter.add(it)
                            }
                        }else{
                            paperGamesInteractor.get(h.key)?.let{
                                if(it.title.contains(search))
                                    listAdapter.add(it)
                            }
                        }
                    }
                    listAdapter = ArrayList(listAdapter.sortedBy { it.title })
                    view?.setData(listAdapter)
                    view?.showProgress(false)
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.gamesErrorLoading)
                })
    }

    override fun getGroupGamesData() {
        view?.showProgress(true)
        getGroupGamesInteractor
                .getFirebaseDataGroupGames(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    var listAdapter = arrayListOf<Game>()
                    for (h in it.children) {
                        if (search.equals("")) {
                            paperGamesInteractor.get(h.key)?.let{
                                listAdapter.add(it)
                            }

                        }else{
                            paperGamesInteractor.get(h.key)?.let{
                                if(it.title.contains(search))
                                    listAdapter?.add(it)
                            }
                        }
                    }
                    listAdapter = ArrayList(listAdapter.sortedBy { it.title })
                    view?.setData(listAdapter)
                    view?.showProgress(false)
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.gamesErrorLoading)
                })
    }

    override fun getPlaceGamesData() {
        view?.showProgress(true)
        getPlaceGamesInteractor
                .getFirebaseDataPlaceGames(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    var listAdapter = arrayListOf<Game>()
                    for (h in it.children) {
                        if (search.equals("")) {
                            paperGamesInteractor.get(h.key)?.let{
                                listAdapter.add(it)
                            }
                        }else{
                            paperGamesInteractor.get(h.key)?.let{
                                if(it.title.contains(search))
                                    listAdapter.add(it)
                            }
                        }
                    }
                    listAdapter = ArrayList(listAdapter.sortedBy { it.title })
                    view?.setData(listAdapter)
                    view?.showProgress(false)
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.gamesErrorLoading)
                })
    }

    override fun addRemoveItem(adding: Boolean, itemId: String, kind: String) {
        val id = kind.substring(6,kind.length)
        if(adding) {
            if (kind.contains("buddy-"))
                addGameToUserInteractor
                        .addFirebaseDataGameToUser(id, itemId)
                        .subscribe({
                            view?.showSuccess(R.string.gamesSuccessAddingToUser)
                        },{
                            view?.showError(R.string.gamesErrorAddingToUser)
                        })
            else if (kind.contains("group-"))
                addGameToGroupInteractor
                        .AddFirebaseDataGameToGroup(id, itemId)
                        .subscribe({
                            view?.showSuccess(R.string.gamesSuccessAddingToGroup)
                        },{
                            view?.showError(R.string.gamesErrorAddingToGroup)
                        })
            else if (kind.contains("place-"))
                addGameToPlaceInteractor
                        .AddFirebaseDataGameToPlace(id, itemId)
                        .subscribe({
                            view?.showSuccess(R.string.gamesSuccessAddingToPlace)
                        },{
                            view?.showError(R.string.gamesErrorAddingToPlace)
                        })
            else
                view?.showError(R.string.gamesErrorRemoveDatabase)
        }else{
            if (kind.contains("buddy-"))
                removeGameToUserInteractor
                        .removeFirebaseDataGameToUser(id, itemId)
                        .subscribe({
                            view?.showSuccess(R.string.gamesSuccessRemoveToUser)
                        },{
                            view?.showError(R.string.gamesErrorRemoveToUser)
                        })
            else if (kind.contains("group-"))
                removeGameToGroupInteractor
                        .removeFirebaseDataGameToGroup(id, itemId)
                        .subscribe({
                            view?.showSuccess(R.string.gamesSuccessRemoveToGroup)
                        },{
                            view?.showError(R.string.gamesErrorRemoveToGroup)
                        })
            else if (kind.contains("place-"))
                removeGameToPlaceInteractor
                        .RemoveFirebaseDataGameToPlace(id, itemId)
                        .subscribe({
                            view?.showSuccess(R.string.gamesSuccessRemoveFromPlace)
                        },{
                            view?.showError(R.string.gamesErrorRemoveToPlace)
                        })
            else
                view?.showError(R.string.gamesErrorRemoveDatabase)
        }
    }

}