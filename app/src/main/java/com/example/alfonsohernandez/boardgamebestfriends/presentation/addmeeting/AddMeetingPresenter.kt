package com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetGroupGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetPlaceGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetSingleGameInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetUserGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetUserGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingToGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingToPlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetOpenPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetUserPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class AddMeetingPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                              private val addMeetingInteractor: AddMeetingInteractor,
                                              private val addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                              private val addMeetingToGroupInteractor: AddMeetingToGroupInteractor,
                                              private val addMeetingToPlaceInteractor: AddMeetingToPlaceInteractor,
                                              private val getUserGroupsInteractor: GetUserGroupsInteractor,
                                              private val getSingleGroupInteractor: GetSingleGroupInteractor,
                                              private val getOpenPlacesInteractor: GetOpenPlacesInteractor,
                                              private val getUserPlacesInteractor: GetUserPlacesInteractor,
                                              private val getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                              private val getUserGamesInteractor: GetUserGamesInteractor,
                                              private val getGroupGamesInteractor: GetGroupGamesInteractor,
                                              private val getPlaceGamesInteractor: GetPlaceGamesInteractor,
                                              private val getSingleGameInteractor: GetSingleGameInteractor,
                                              private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddMeetingContract.Presenter {

    private val TAG = "AddMeetingPresenter"

    private var view: AddMeetingContract.View? = null

    var groupList: ArrayList<Group> = arrayListOf()
    var placeList: ArrayList<Place> = arrayListOf()
    var gameList: ArrayList<Game> = arrayListOf()

    var myPlace: Place? = null

    fun setView(view: AddMeetingContract.View?) {
        this.view = view
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun saveMeeting(meeting: Meeting, playing: Boolean) {

        addMeetingInteractor
                .addFirebaseDataMeeting(getUserProfile()!!.regionId,meeting)
                .subscribe({
                    firebaseEvent("Adding meeting",TAG)
                    if(playing)
                        addMeetingToUserInteractor
                                .addFirebaseDataMeetingToUser(getUserProfile()!!.regionId,getUserProfile()!!.id,playing,meeting.id)
                                .subscribe({},{})
                    if(!meeting.groupId!!.equals("Open"))
                        addMeetingToGroupInteractor
                                .addFirebaseDataMeetingToGroup(meeting.groupId,meeting.id)
                                .subscribe({},{})
                    if(!meeting.placeId!!.equals("My place"))
                        addMeetingToPlaceInteractor
                                .addFirebaseDataMeetingToPlace(meeting.placeId,meeting.id)
                                .subscribe({},{})
                    view?.finishAddMeeting()
                },{
                    view?.showErrorAddingMeeting()
                })
    }

    override fun getUserGroups() {
        view?.showProgressBar(true)
        getUserGroupsInteractor
                .getFirebaseDataUserGroups(getUserProfile()!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        getSingleGroup(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGroups()
                })
    }

    override fun getSingleGroup(groupId: String) {
        view?.showProgressBar(true)
        getSingleGroupInteractor
                .getFirebaseDataSingleGroup(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    groupList.add(it.getValue(Group::class.java)!!)
                    view?.addGroupToSpinner(it.getValue(Group::class.java)!!.title)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGroups()
                })
    }

    override fun getOpenPlaces() {
        view?.showProgressBar(true)
        getOpenPlacesInteractor
                .getFirebaseDataOpenPlaces(getUserProfile()!!.regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var placesListName = arrayListOf<String>()
                    for (h in it.children) {
                        placeList.add(h.getValue(Place::class.java)!!)
                        placesListName.add(h.getValue(Place::class.java)!!.name)
                    }
                    view?.setPlaceSpinner(placesListName)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorPlaces()
                })
    }

    override fun getUserPlaces() {
        view?.showProgressBar(true)
        getUserPlacesInteractor
                .getFirebaseDataUserPlaces(getUserProfile()!!.regionId,getUserProfile()!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        getSinglePlace(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorPlaces()
                })
    }

    override fun getSinglePlace(placeId: String) {
        view?.showProgressBar(true)
        getSinglePlaceInteractor
                .getFirebaseDataSinglePlace(getUserProfile()!!.regionId,placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var actualPlace = it.getValue(Place::class.java)
                    if(actualPlace!!.openPlace)
                        myPlace = actualPlace
                },{
                    view?.showProgressBar(false)
                    view?.showErrorPlaces()
                })
    }

    override fun getMyPlacee(): Place? {
        return myPlace
    }

    override fun getUserGames(userId: String) {
        view?.showProgressBar(true)
        getUserGamesInteractor
                .getFirebaseDataUserGames(getUserProfile()!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        getSingleGame(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGames()
                })
    }

    override fun getGroupGames(groupId: String) {
        view?.showProgressBar(true)
        getGroupGamesInteractor
                .getFirebaseDataGroupGames(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        getSingleGame(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGames()
                })
    }

    override fun getPlaceGames(placeId: String) {
        view?.showProgressBar(true)
        getPlaceGamesInteractor
                .getFirebaseDataPlaceGames(placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        getSingleGame(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGames()
                })
    }

    override fun getSingleGame(gameId: String) {
        view?.showProgressBar(true)
        getSingleGameInteractor
                .getFirebaseDataSingleGame(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    gameList.add(it.getValue(Game::class.java)!!)
                    view?.addGameToSpinner(it.getValue(Game::class.java)!!.title)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGames()
                })
    }

    override fun getGroupFromTitle(groupTitle: String): Group {
        var tempGroupList = groupList
        return tempGroupList.filter { it.title.equals(groupTitle) }.get(0)
    }

    override fun getPlaceFromTitle(placeName: String): Place {
        var tempPlaceList = placeList
        return tempPlaceList.filter { it.name.equals(placeName) }.get(0)
    }

    override fun getGameFromTitle(gameTitle: String): Game {
        var tempGameList = gameList
        return tempGameList.filter { it.title.equals(gameTitle) }.get(0)
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }
}