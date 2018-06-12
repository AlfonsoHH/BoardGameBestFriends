package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetSingleGameInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.GetSingleMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.ModifyMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.RemoveMeetingToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetMeetingUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.ClearTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.SetTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MeetingDetailPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                                 private val paperMeetingsInteractor: PaperMeetingsInteractor,
                                                 private val getSingleGameInteractor: GetSingleGameInteractor,
                                                 private val getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                                 private val getMeetingUsersInteractor: GetMeetingUsersInteractor,
                                                 private val getSingleUserInteractor: GetSingleUserInteractor,
                                                 private val addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                                 private val modifyMeetingInteractor: ModifyMeetingInteractor,
                                                 private val removeMeetingToUserInteractor: RemoveMeetingToUserInteractor,
                                                 private val setTopicInteractor: SetTopicInteractor,
                                                 private val clearTopicInteractor: ClearTopicInteractor,
                                                 private val firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): MeetingDetailContract.Presenter, BasePresenter<MeetingDetailContract.View>() {

    private val TAG = "MeetingDetailPresenter"

    var meetingId: String = ""
    var actualMeeting = Meeting()

    fun setView(view: MeetingDetailContract.View?, meetingId: String) {
        this.view = view
        this.meetingId = meetingId
        view?.showProgress(true)
        if(!meetingId.equals("")) {
            getMeetingData(meetingId)
            firebaseEvent("Showing a Meeting", TAG)
        }
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        firebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun joinGame(boolean: Boolean) {
        getUserProfile()?.let { user ->
            if (boolean)
                addMeetingToUserInteractor
                        .addFirebaseDataMeetingToUser(user.regionId, user.id, true, meetingId)
                        .subscribe({
                            setTopicInteractor.setFCMtopic(meetingId)
                            view?.setFriendData(user, true)
                            modifyMeeting(-1)
                        }, {
                            view?.showError(R.string.meetingDetailErrorJoiningMeeting)
                        })
            else
                if (!actualMeeting.creatorId.equals(user.id)) {
                    removeMeetingToUserInteractor
                            .removeFirebaseDataMeeting(user.regionId, user.id, meetingId)
                            .subscribe({
                                clearTopicInteractor.clearFCMtopic(meetingId)
                                view?.removeFriendData(user, true)
                                modifyMeeting(1)
                            }, {
                                view?.showError(R.string.meetingDetailErrorLevingMeeting)
                            })
                } else {
                    addMeetingToUserInteractor
                            .addFirebaseDataMeetingToUser(user.regionId, user.id, false, meetingId)
                            .subscribe({
                                clearTopicInteractor.clearFCMtopic(meetingId)
                                view?.removeFriendData(user, true)
                                modifyMeeting(1)
                            }, {
                                view?.showError(R.string.meetingDetailErrorLevingMeeting)
                            })
                }
        }
    }

    fun modifyMeeting(spot: Int){
        getUserProfile()?.let { user ->
            actualMeeting.vacants = actualMeeting.vacants + spot
            modifyMeetingInteractor
                    .modifyFirebaseDataMeeting(user.regionId, actualMeeting.id, actualMeeting)
                    .subscribe({
                        paperMeetingsInteractor.update(actualMeeting)
                        if (spot == -1)
                            view?.showSuccess(R.string.meetingDetailSuccessJoining)
                        else if (spot == 1)
                            view?.showSuccess(R.string.meetingDetailSuccessLeaving)
                    }, {
                        view?.showError(R.string.meetingDetailErrorVacant)
                    })
        }
    }

    override fun getMeetingOpenHours(place: Place): String {
        place.firstOpeningHours?.let {
            if (!it.equals("")) {
                if (happenToday(it.substring(0, it.lastIndexOf("_")))) {
                    return it.substring(it.lastIndexOf("_") + 1, it.length)
                }
            }
        }
        place.secondOpeningHours?.let {
            if (!it.equals("")) {
                if (happenToday(it.substring(0, it.lastIndexOf("_")))) {
                    return it.substring(it.lastIndexOf("_") + 1, it.length)
                }
            }
        }
        place.thirdOpeningHours?.let {
            if (!it.equals("")) {
                if (happenToday(it.substring(0, it.lastIndexOf("_")))) {
                    return it.substring(it.lastIndexOf("_") + 1, it.length)
                }
            }
        }
        return "default"

    }

    fun happenToday(days: String): Boolean{
        var allDays = days
        if(days.contains("-")) {
            allDays = allDays + severalDays(days.substring(days.lastIndexOf("-") - 3, days.lastIndexOf("-") + 4))
        }
        val dateMeeting = SimpleDateFormat("dd/MM/yy").parse(actualMeeting.date.substring(actualMeeting.date.length-8,actualMeeting.date.length))
        val dateMeetingDayWeek = SimpleDateFormat("EEE").format(dateMeeting)
        if(allDays.contains(dateMeetingDayWeek)){
            return true
        }
        return false
    }

    fun severalDays(days: String): String{
        val numberDayBegin = returnDayNumber(days.substring(0,days.lastIndexOf("-")))
        val numberDayEnd = returnDayNumber(days.substring(days.lastIndexOf("-")+1,days.length))
        val numbers = arrayListOf<Int>()
        for(i in numberDayBegin..numberDayEnd){
            numbers.add(i)
        }
        var severalDays = ""
        for (day in numbers){
            severalDays = severalDays + " " + returnNumberDays(day)
        }
        return severalDays
    }

    fun returnNumberDays(day: Int): String{
        when(day){
            1 -> return "Mon"
            2 -> return "Tue"
            3 -> return "Wed"
            4 -> return "Thu"
            5 -> return "Fri"
            6 -> return "Sat"
            7 -> return "Sun"
        }
        return ""
    }

    fun returnDayNumber(day: String): Int{
        when(day){
            "Mon" -> return 1
            "Tue" -> return 2
            "Wed" -> return 3
            "Thu" -> return 4
            "Fri" -> return 5
            "Sat" -> return 6
            "Sun" -> return 7
        }
        return -1
    }

    override fun getMeetingData(meetingId: String) {
        paperMeetingsInteractor.get(meetingId)?.let {
            actualMeeting = it
            view?.setMeetingData(actualMeeting)
            getGameData(actualMeeting.gameId)
            getPlaceData(actualMeeting.placeId)
            getFriendsData(meetingId)
        }
    }

    override fun getGameData(gameId: String) {
        view?.showProgress(true)
        getSingleGameInteractor
                .getFirebaseDataSingleGame(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    view?.setGameData(it.getValue(Game::class.java)!!)
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.meetingDetailErrorGame)
                })
    }

    override fun getPlaceData(placeId: String) {
        getUserProfile()?.let { user ->
            view?.showProgress(true)
            getSinglePlaceInteractor
                    .getFirebaseDataSinglePlace(user.regionId, placeId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        view?.setPlaceData(it.getValue(Place::class.java)!!)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.meetingDetailErrorPlace)
                    })
        }
    }

    override fun getFriendsData(meetingId: String) {
        getUserProfile()?.let { user ->
            view?.showProgress(true)
            getMeetingUsersInteractor
                    .getFirebaseDataMeetingUsers(user.regionId, meetingId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        view?.clearFriendsData()
                        for (h in it.children) {
                            if (h.getValue(Boolean::class.java)!!)
                                getFriendData(h.key)
                        }
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.meetingDetailErrorMembers)
                    })
        }
    }

    override fun getFriendData(friendId: String) {
        view?.showProgress(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(friendId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    getUserProfile()?.let {user ->
                        if (user.id.equals(friendId))
                            view?.setFriendData(it.getValue(User::class.java)!!, true)
                        else
                            view?.setFriendData(it.getValue(User::class.java)!!, false)
                    }
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.meetingDetailErrorMembers)
                })
    }
}