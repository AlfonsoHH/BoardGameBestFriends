package com.example.alfonsohernandez.boardgamebestfriends.presentation.tab

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetAllGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.ModifyUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperGamesRepository
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePushPresenter
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class TabPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                       private val getPlacesInteractor: GetPlacesInteractor,
                                       private val paperPlacesInteractor: PaperPlacesInteractor,
                                       private val paperGamesInteractor: PaperGamesInteractor,
                                       private val getAllGamesInteractor: GetAllGamesInteractor,
                                       private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor
                                        ): TabContract.Presenter,
                                            BasePresenter<TabContract.View>() {

    private val TAG: String = "TabPresenter"

    var compositeDisposable = CompositeDisposable()

    fun unsetView(){
        this.view = null
        compositeDisposable.dispose()
    }

    fun setView(view: TabContract.View?) {
        this.view = view
        view?.setData()
        loadPlacesData()
        loadAllGames()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    fun loadPlacesData() {
        getUserProfile()?.let { user ->
            compositeDisposable.add(getPlacesInteractor
                    .getFirebaseDataOpenPlaces(user.regionId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        val placesList = arrayListOf<Place>()
                        for (h in it.children) {
                            val place = h.getValue(Place::class.java)
                            if (place != null) {
                                if (place.openPlace)
                                    placesList.add(place)
                                if (!place.openPlace && place.ownerId.equals(user.id))
                                    placesList.add(place)
                            }
                        }
                        paperPlacesInteractor.clear()
                        paperPlacesInteractor.addAll(placesList)
                    }, {
                        view?.showError(R.string.placesErrorPlaces)
                    },{
                        paperPlacesInteractor.clear()
                    }))
        }
    }

    fun loadAllGames() {
        paperGamesInteractor.clear()
        compositeDisposable.add(getAllGamesInteractor
                .getFirebaseDataAllGames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    var gameList = arrayListOf<Game>()
                    for (h in it.children) {
                        gameList.add(h.getValue(Game::class.java)!!)
                    }
                    gameList = ArrayList(gameList.sortedBy {game -> game.title })
                    paperGamesInteractor.addAll(gameList)
                },{
                    view?.showError(R.string.gamesErrorLoading)
                }))
    }

}