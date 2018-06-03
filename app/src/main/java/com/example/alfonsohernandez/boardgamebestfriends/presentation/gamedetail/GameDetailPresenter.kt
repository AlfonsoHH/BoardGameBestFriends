package com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetSingleGameInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class GameDetailPresenter @Inject constructor(private val getSingleGameInteractor: GetSingleGameInteractor,
                                              private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor) : GameDetailContract.Presenter {

    private val TAG = "GameDetailPresenter"

    private var view: GameDetailContract.View? = null

    fun setView(view: GameDetailContract.View?, gameId: String) {
        this.view = view
        if (!gameId.equals(""))
            getGameData(gameId)
        firebaseEvent("Showing game", TAG)
    }

    override fun getGameData(gameId: String) {
        view?.showProgressBar(true)
        getSingleGameInteractor
                .getFirebaseDataSingleGame(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setData(it.getValue(Game::class.java)!!)
                }, {
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id, activityName)
    }
}