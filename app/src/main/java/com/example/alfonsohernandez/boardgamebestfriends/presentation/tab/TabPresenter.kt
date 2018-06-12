package com.example.alfonsohernandez.boardgamebestfriends.presentation.tab

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetAllGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.ModifyUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperGamesRepository
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class TabPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                       private val paperRegionsInteractor: PaperRegionsInteractor,
                                       private val paperGamesInteractor: PaperGamesInteractor,
                                       private val paperMeetingsInteractor: PaperMeetingsInteractor,
                                       private val paperPlacesInteractor: PaperPlacesInteractor,
                                       private val getAllGamesInteractor: GetAllGamesInteractor,
                                       private val modifyUserInteractor: ModifyUserInteractor,
                                       private val saveUserProfileInteractor: SaveUserProfileInteractor,
                                       private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): TabContract.Presenter, BasePresenter<TabContract.View>() {

    private val TAG: String = "TabPresenter"

    fun setView(view: TabContract.View?) {
        this.view = view
        view?.setData()
        loadAllGames()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun clearPaper() {
        paperMeetingsInteractor.clear()
        paperPlacesInteractor.clear()
    }

    fun loadAllGames() {
        paperGamesInteractor.clear()
        getAllGamesInteractor
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
                })
    }

    override fun modifyUserInFirebaseDB(userId: String, user: User) {
        modifyUserInteractor
                .modifyFirebaseDataUser(userId,user)
                .subscribe({
                    saveUserInPaper(user)
                },{
                    view?.showError(R.string.tabErrorUser)
                })
    }

    override fun saveUserInPaper(user: User) {
        saveUserProfileInteractor
                .save(user)
                .subscribe({
                    firebaseEvent("Login in", TAG)
                    view?.successChangingRegion()
                },{
                    view?.showError(R.string.tabErrorUser)
                })
    }

    override fun getRegionList(): ArrayList<Region>{
        return paperRegionsInteractor.all()
    }

    override fun getRegionId(cityName: String): String {
        var regionId = ""
        for(region in paperRegionsInteractor.all()){
            if(region.city.equals(cityName))
                regionId = region.id
        }
        return regionId
    }
}