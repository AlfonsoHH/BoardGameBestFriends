package com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetSingleGameInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamedetail.GetBggGameDetailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class GameDetailPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                              private val paperGamesInteractor: PaperGamesInteractor,
                                              private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor) : GameDetailContract.Presenter, BasePresenter<GameDetailContract.View>() {

    private val TAG = "GameDetailPresenter"

    fun setView(view: GameDetailContract.View?, gameId: String) {
        this.view = view
        if (!gameId.equals(""))
            getGameData(gameId)
        firebaseEvent("Showing game", TAG)
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id, activityName)
    }

    override fun getGameData(gameId: String) {
        paperGamesInteractor.get(gameId)?.let {
            view?.setData(it)
        }
    }

}