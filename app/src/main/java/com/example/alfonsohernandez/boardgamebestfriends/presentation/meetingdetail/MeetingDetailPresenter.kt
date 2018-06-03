package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.GetSingleGameInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.AddMeetingToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.GetSingleMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.ModifyMeetingInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.RemoveMeetingToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetSinglePlaceInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetMeetingUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MeetingDetailPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                                 private val getSingleMeetingInteractor: GetSingleMeetingInteractor,
                                                 private val getSingleGameInteractor: GetSingleGameInteractor,
                                                 private val getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                                 private val getMeetingUsersInteractor: GetMeetingUsersInteractor,
                                                 private val getSingleUserInteractor: GetSingleUserInteractor,
                                                 private val addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                                 private val modifyMeetingInteractor: ModifyMeetingInteractor,
                                                 private val removeMeetingToUserInteractor: RemoveMeetingToUserInteractor,
                                                 private val firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): MeetingDetailContract.Presenter {

    private val TAG = "MeetingDetailPresenter"

    private var view: MeetingDetailContract.View? = null
    var meetingId: String = ""

    var actualMeeting = Meeting()

    fun setView(view: MeetingDetailContract.View?, meetingId: String) {
        this.view = view
        this.meetingId = meetingId
        view?.showProgressBar(true)
        if(!meetingId.equals("")) {
            getMeetingData(meetingId)
            firebaseEvent("Showing a Meeting", TAG)
        }
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun joinGame(boolean: Boolean) {
        if(boolean)
            addMeetingToUserInteractor
                    .addFirebaseDataMeetingToUser(getUserProfile()!!.regionId,getUserProfile()!!.id,true,meetingId)
                    .subscribe({
                        modifyMeeting(-1)
                    },{
                        view?.showErrorJoining()
                    })
        else
            if(!actualMeeting.creatorId.equals(getUserProfile()!!.id)) {
                removeMeetingToUserInteractor
                        .removeFirebaseDataMeeting(getUserProfile()!!.regionId, getUserProfile()!!.id, meetingId)
                        .subscribe({
                            modifyMeeting(1)
                        }, {
                            view?.showErrorLeaving()
                        })
            }else{
                addMeetingToUserInteractor
                        .addFirebaseDataMeetingToUser(getUserProfile()!!.regionId,getUserProfile()!!.id,false,meetingId)
                        .subscribe({
                            modifyMeeting(1)
                        },{
                            view?.showErrorLeaving()
                        })
            }
    }

    fun modifyMeeting(spot: Int){
        actualMeeting.vacants = actualMeeting.vacants + spot
        modifyMeetingInteractor
                .modifyFirebaseDataMeeting(getUserProfile()!!.regionId,actualMeeting.id,actualMeeting)
                .subscribe({
                    if(spot == -1)
                        view?.successJoining()
                    else if(spot == 1)
                        view?.successLeaving()
                },{
                    view?.showErrorVacant()
                })
    }

    override fun getMeetingOpenHours(place: Place): String {
        if(!place.firstOpeningHours.equals("")) {
            if (happenToday(place.firstOpeningHours!!.substring(0, place.firstOpeningHours!!.lastIndexOf("_")))) {
                return place.firstOpeningHours!!.substring(place.firstOpeningHours!!.lastIndexOf("_")+1,place.firstOpeningHours!!.length)
            }
        }
        if(!place.secondOpeningHours.equals("")) {
            if (happenToday(place.secondOpeningHours!!.substring(0, place.secondOpeningHours!!.lastIndexOf("_")))) {
                return place.secondOpeningHours!!.substring(place.secondOpeningHours!!.lastIndexOf("_")+1,place.secondOpeningHours!!.length)
            }
        }
        if(!place.thirdOpeningHours.equals("")) {
            if (happenToday(place.thirdOpeningHours!!.substring(0, place.thirdOpeningHours!!.lastIndexOf("_")))) {
                return place.thirdOpeningHours!!.substring(place.thirdOpeningHours!!.lastIndexOf("_")+1,place.thirdOpeningHours!!.length)
            }
        }

        return "default"

    }

    fun happenToday(days: String): Boolean{
        var allDays = days
        if(days.contains("-")) {
            allDays = allDays + severalDays(days.substring(days.lastIndexOf("-") - 3, days.lastIndexOf("-") + 4))
        }
        var dateMeeting = SimpleDateFormat("dd/MM/yy").parse(actualMeeting.date.substring(actualMeeting.date.length-8,actualMeeting.date.length))
        var dateMeetingDayWeek = SimpleDateFormat("EEE").format(dateMeeting)
        if(allDays.contains(dateMeetingDayWeek)){
            return true
        }
        return false
    }

    fun severalDays(days: String): String{
        var numberDayBegin = returnDayNumber(days.substring(0,days.lastIndexOf("-")))
        var numberDayEnd = returnDayNumber(days.substring(days.lastIndexOf("-")+1,days.length))
        var numbers = arrayListOf<Int>()
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
        view?.showProgressBar(true)
        getSingleMeetingInteractor
                .getFirebaseDataSingleMeeting(getUserProfile()!!.regionId,meetingId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    actualMeeting = it.getValue(Meeting::class.java)!!
                    view?.setMeetingData(actualMeeting!!)
                    getGameData(actualMeeting!!.gameId)
                    getPlaceData(actualMeeting!!.placeId)
                    getFriendsData(meetingId)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorMeeting()
                })
    }

    override fun getGameData(gameId: String) {
        view?.showProgressBar(true)
        getSingleGameInteractor
                .getFirebaseDataSingleGame(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setGameData(it.getValue(Game::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGame()
                })
    }

    override fun getPlaceData(placeId: String) {
        view?.showProgressBar(true)
        getSinglePlaceInteractor
                .getFirebaseDataSinglePlace(getUserProfile()!!.regionId,placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setPlaceData(it.getValue(Place::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorPlace()
                })
    }

    override fun getFriendsData(meetingId: String) {
        view?.showProgressBar(true)
        getMeetingUsersInteractor
                .getFirebaseDataMeetingUsers(getUserProfile()!!.regionId,meetingId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.clearFriendsData()
                    for (h in it.children) {
                        getFriendData(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorMembers()
                })
    }

    override fun getFriendData(friendId: String) {
        view?.showProgressBar(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(friendId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    if(getUserProfile()!!.id.equals(friendId))
                        view?.setFriendData(it.getValue(User::class.java)!!,true)
                    else
                        view?.setFriendData(it.getValue(User::class.java)!!,false)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorMembers()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        firebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }
}