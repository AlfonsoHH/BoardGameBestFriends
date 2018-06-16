package com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetGroupGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetPlaceGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetSingleGameInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetUserGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.GetSingleMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.ModifyMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetUserPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetMeetingUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.*
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
class AddMeetingPresenter @Inject constructor(private val fcmHandler: FCMHandler,
                                              private val getUserProfileInteractor: GetUserProfileInteractor,
                                              private val paperMeetingsInteractor: PaperMeetingsInteractor,
                                              private val paperGroupsInteractor: PaperGroupsInteractor,
                                              private val paperPlacesInteractor: PaperPlacesInteractor,
                                              private val paperGamesInteractor: PaperGamesInteractor,
                                              private val addMeetingInteractor: AddMeetingInteractor,
                                              private val addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                              private val modifyMeetingInteractor: ModifyMeetingInteractor,
                                              private val getSingleMeetingInteractor: GetSingleMeetingInteractor,
                                              private val getUserGamesInteractor: GetUserGamesInteractor,
                                              private val getGroupGamesInteractor: GetGroupGamesInteractor,
                                              private val getPlaceGamesInteractor: GetPlaceGamesInteractor,
                                              private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor
                                            ) : AddMeetingContract.Presenter,
                                                BasePushPresenter<AddMeetingContract.View>() {

    private val TAG = "AddMeetingPresenter"

    var myPlace: Place? = null

    var compositeDisposable = CompositeDisposable()

    fun unsetView(){
        this.view = null
        compositeDisposable.dispose()
    }

    fun setView(view: AddMeetingContract.View?) {
        this.view = view
        fcmHandler.push = this
    }

    override fun pushReceived(rm: RemoteMessage) {
        view?.showNotification(rm)
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id, activityName)
    }

    override fun getMeetingData(meetingId: String) {
        paperMeetingsInteractor.get(meetingId)?.let { meeting ->
            view?.setData(meeting, paperPlacesInteractor.get(meeting.placeId)!!,paperGroupsInteractor.get(meeting.groupId)!!, paperGamesInteractor.get(meeting.gameId)!!)
        }
    }

    override fun saveMeeting(meeting: Meeting, playing: Boolean) {
        getUserProfile()?.let { user ->
            addMeetingInteractor
                    .addFirebaseDataMeeting(user.regionId, meeting)
                    .subscribe({
                        firebaseEvent("Adding meeting", TAG)
                        if (playing) {
                            addMeetingToUserInteractor
                                    .addFirebaseDataMeetingToUser(user.regionId, user.id, playing, meeting.id)
                                    .subscribe({
                                        paperMeetingsInteractor.add(meeting)
                                        view?.finishAddMeeting()
                                    }, {
                                        view?.showError(R.string.addMeetingErrorMeetingUser)
                                    })
                        }
                        paperMeetingsInteractor.add(meeting)
                        view?.finishAddMeeting()
                    }, {
                        view?.showError(R.string.addMeetingErrorAdding)
                    })
        }
    }

    override fun modifyMeeting(meeting: Meeting, playing: Boolean) {
        getUserProfile()?.let { user ->
            view?.showProgress(true)
            compositeDisposable.add(getSingleMeetingInteractor
                    .getFirebaseDataSingleMeeting(user.regionId,meeting.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        var previousMeeting = it.getValue(Meeting::class.java)!!
                        meeting.groupId = previousMeeting.groupId
                        meeting.placeId = previousMeeting.placeId
                        meeting.placePhoto = previousMeeting.placePhoto
                        meeting.gameId = previousMeeting.gameId
                        meeting.gamePhoto = previousMeeting.gamePhoto
                        meeting.vacants = previousMeeting.vacants
                        modifyMeetingInteractor
                                .modifyFirebaseDataMeeting(user.regionId, meeting.id, meeting)
                                .subscribe({
                                    firebaseEvent("Modifying meeting", TAG)
                                    paperMeetingsInteractor.update(meeting)
                                    view?.finishAddMeeting()
                                }, {
                                    view?.showError(R.string.addMeetingErrorAdding)
                                })
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.addMeetingErrorMeetingInfo)
                    }))
        }
    }

    override fun getUserGroups(): ArrayList<String> {
        val titleList = arrayListOf<String>()
        for (group in paperGroupsInteractor.all()) {
            titleList.add(group.title)
        }
        return titleList
    }

    override fun getPlaces(): ArrayList<String> {
        val placesListName = arrayListOf<String>()
        for (place in paperPlacesInteractor.all()) {
            if (place.openPlace)
                placesListName.add(place.name)
        }
        return placesListName
    }

    override fun getMyPlacee(): Place? {
        for (place in paperPlacesInteractor.all()) {
            if (!place.openPlace && place.ownerId.equals(getUserProfile()?.id))
                myPlace = place
        }
        return myPlace
    }

    override fun getUserGames(userId: String) {
        getUserProfile()?.let { user ->
            view?.showProgress(true)
            compositeDisposable.add(getUserGamesInteractor
                    .getFirebaseDataUserGames(user.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        var listGames = arrayListOf<Game>()
                        for (h in it.children) {
                            paperGamesInteractor.get(h.key)?.let { game ->
                                listGames.add(game)
                            }
                        }
                        listGames = ArrayList(listGames.sortedBy { it.title })
                        view?.addGamesToSpinner(listGames)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.addMeetingErrorGames)
                    }))
        }
    }

    override fun getGroupGames(groupId: String?) {
        if (groupId != null) {
            view?.showProgress(true)
            compositeDisposable.add(getGroupGamesInteractor
                    .getFirebaseDataGroupGames(groupId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        for (h in it.children) {
                            paperGamesInteractor.get(h.key)?.let { game ->
                                view?.addGameToSpinner(game.title)
                            }
                        }
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.addMeetingErrorGames)
                    }))
        }
    }

    override fun getPlaceGames(placeId: String?) {
        if (placeId != null) {
            view?.showProgress(true)
            compositeDisposable.add(getPlaceGamesInteractor
                    .getFirebaseDataPlaceGames(placeId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        for (h in it.children) {
                            paperGamesInteractor.get(h.key)?.let { game ->
                                view?.addGameToSpinner(game.title)
                            }
                        }
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.addMeetingErrorGames)
                    }))
        }
    }

    override fun getGroupFromTitle(groupTitle: String): Group? {
        val tempGroupList = paperGroupsInteractor.all()
        if (tempGroupList.size > 0)
            return tempGroupList.filter { it.title.equals(groupTitle) }.get(0)
        else
            return null
    }

    override fun getPlaceFromTitle(placeName: String): Place? {
        val tempPlaceList = paperPlacesInteractor.all()
        if (placeName.equals("My Place")) {
            var myPlace: Place? = null
            for (place in tempPlaceList) {
                if (!place.openPlace && place.ownerId.equals(getUserProfile()?.id))
                    myPlace = place
            }
            return myPlace
        } else {
            if (tempPlaceList.size > 0)
                return tempPlaceList.filter { it.name.equals(placeName) }.get(0)
            else
                return null
        }
    }

    override fun getGameFromTitle(gameTitle: String): Game {
        val tempGameList = paperGamesInteractor.all()
        return tempGameList.filter { it.title.equals(gameTitle) }.get(0)
    }

}