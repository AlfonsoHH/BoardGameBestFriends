package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetUserGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetUserPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.ClearTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.SetTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperGamesRepository
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperMeetingsRepository
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperPlacesRepository
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePushPresenter
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class MeetingsPresenter @Inject constructor(private val fcmHandler: FCMHandler,
                                            private val getUserProfileInteractor: GetUserProfileInteractor,
                                            private val paperMeetingsInteractor: PaperMeetingsInteractor,
                                            private val getOpenMeetingsInteractor: GetOpenMeetingsInteractor,
                                            private val getUserMeetingsInteractor: GetUserMeetingsInteractor,
                                            private val getUserGroupsInteractor: GetUserGroupsInteractor,
                                            private val getUserPlacesInteractor: GetUserPlacesInteractor,
                                            private val getGroupMeetingsInteractor: GetGroupMeetingsInteractor,
                                            private val getPlaceMeetingsInteractor: GetPlaceMeetingsInteractor,
                                            private val setTopicInteractor: SetTopicInteractor,
                                            private val clearTopicInteractor: ClearTopicInteractor,
                                            private val removeMeetingInteractor: RemoveMeetingInteractor,
                                            private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor
                                            ) : MeetingsContract.Presenter,
                                                BasePushPresenter<MeetingsContract.View>() {

    var kind = ""
    var search = ""

    var allMeetings = arrayListOf<Meeting>()

    private val TAG = "MeetingsPresenter"

    var compositeDisposable = CompositeDisposable()

    fun unsetViewFragment(){
        this.view = null
        compositeDisposable.clear()
    }

    fun unsetView(){
        this.view = null
        compositeDisposable.dispose()
    }

    fun setView(view: MeetingsContract.View?, kind: String) {
        this.view = view
        this.kind = kind
        initialDataChooser()
        fcmHandler.push = this
    }

    override fun pushReceived(rm: RemoteMessage) {
        view?.showNotification(rm)
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    fun setListTopics(meetingList: ArrayList<Meeting>) {
        for (meeting in meetingList) {
            setTopicInteractor.setFCMtopic(meeting.id)
        }
    }

    override fun initialDataChooser() {
        view?.getSearchData()?.let {
            search = it
        }
        if (kind.contains("buddy-"))
            loadAllMeetings("buddy-")
        else if (kind.contains("group-"))
            loadAllMeetings("group-")
        else if (kind.contains("place-"))
            loadAllMeetings("place-")
        else
            loadAllMeetings("")

        //subscribeMeetings()
    }

    override fun cacheDataChooser() {
        view?.getSearchData()?.let {
            search = it
        }
        if (kind.contains("buddy-"))
            getUserMeetings()
        else if (kind.contains("group-"))
            getGroupMeetings()
        else if (kind.contains("place-"))
            getPlaceMeetings()
        else
            getOpenMeetings()
    }

    fun loadAllMeetings(kind: String) {
        getUserProfile()?.let {user ->
            view?.showProgress(true)
            compositeDisposable.add(getOpenMeetingsInteractor
                    .getFirebaseDataOpenMeetings(user.regionId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        allMeetings.clear()
                        for (h in it.children) {
                            val actualMeetings = h.getValue(Meeting::class.java)
                            if (SimpleDateFormat("HH:mm_dd/MM/yy").parse(actualMeetings?.date).after(Date()))
                                actualMeetings?.let { actual ->
                                    allMeetings.add(actual)
                                }
                        }
                        paperMeetingsInteractor.addAll(allMeetings)
                        if (kind.equals("buddy-"))
                            getUserMeetings()
                        else if (kind.equals("group-"))
                            getGroupMeetings()
                        else if (kind.equals("place-"))
                            getPlaceMeetings()
                        else
                            getOpenMeetings()
                        view?.showProgress(false)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.meetingsErrorLoading)
                    }))
        }
    }

    fun getOpenMeetings(){
        val tempMeetingList = arrayListOf<Meeting>()
        for(meeting in paperMeetingsInteractor.all()) {
            meeting.label = ""
            if (meeting.vacants > 0) {
                if (search.isEmpty()) {
                    if (meeting.groupId.equals("open")) {
                        meeting.label = "Open"
                        tempMeetingList.add(meeting)
                    }
                } else {
                    if (meeting.title.contains(search) && meeting.groupId.equals("open")) {
                        meeting.label = "Open"
                        tempMeetingList.add(meeting)
                    }
                }
            }
        }
        getUserGroups(tempMeetingList)
    }

    fun getUserGroups(tempMeetingList: ArrayList<Meeting>) {
        getUserProfile()?.let {user ->
            view?.showProgress(true)
            var tempMeetingList = tempMeetingList
            compositeDisposable.add(getUserGroupsInteractor
                    .getFirebaseDataUserGroups(user.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        for (h in it.children) {
                            for (meeting in paperMeetingsInteractor.all()) {
                                if (meeting.vacants > 0) {
                                    if (search.isEmpty()) {
                                        if (meeting.groupId.equals(h.key)) {
                                            meeting.label = "Group"
                                            tempMeetingList.add(meeting)
                                        }
                                    } else {
                                        if (meeting.title.contains(search) && meeting.groupId.equals(h.key)) {
                                            meeting.label = "Group"
                                            tempMeetingList.add(meeting)
                                        }
                                    }
                                }
                            }
                        }
                        tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length - 2, it.date.length) }, { it.date.substring(it.date.length - 5, it.date.length - 3) }, { it.date.substring(it.date.length - 8, it.date.length - 6) }, { it.date.substring(0, it.date.lastIndexOf("_")) })))
                        setListTopics(tempMeetingList)
                        view?.setData(tempMeetingList)
                        view?.showProgress(false)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.meetingsErrorLoading)
                    }, {
                        tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length - 2, it.date.length) }, { it.date.substring(it.date.length - 5, it.date.length - 3) }, { it.date.substring(it.date.length - 8, it.date.length - 6) }, { it.date.substring(0, it.date.lastIndexOf("_")) })))
                        setListTopics(tempMeetingList)
                        view?.setData(tempMeetingList)
                        view?.showProgress(false)
                    }))
        }
    }

    fun getUserMeetings() {
        val tempMeetingList = arrayListOf<Meeting>()
        getUserProfile()?.let { user ->
            view?.showProgress(true)
            compositeDisposable.add(getUserMeetingsInteractor
                    .getFirebaseDataUserMeetings(user.regionId, kind.substring(6, kind.length))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        for (h in it.children) {
                            for (meeting in paperMeetingsInteractor.all()) {
                                meeting.label = ""
                                if (meeting.id.equals(h.key)) {
                                    if (meeting.creatorId.equals(user.id) && h.getValue(Boolean::class.java)!!)
                                        meeting.label = "Admin Playing"
                                    else if (meeting.creatorId.equals(user.id))
                                        meeting.label = "Admin"
                                    else if (h.getValue(Boolean::class.java)!!)
                                        meeting.label = "Playing"
                                    if (search.isEmpty())
                                        tempMeetingList.add(meeting)
                                    else
                                        if (meeting.title.contains(search))
                                            tempMeetingList.add(meeting)
                                }
                            }
                        }
                        getUserPlaces(tempMeetingList)
                        view?.showProgress(false)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.meetingsErrorLoading)
                    },{
                        view?.showProgress(false)
                        getUserPlaces(tempMeetingList)
                    }))
        }
    }

    fun getUserPlaces(tempMeetingList: ArrayList<Meeting>) {
        getUserProfile()?.let { user ->
            var tempMeetingList = tempMeetingList
            view?.showProgress(true)
            compositeDisposable.add(getUserPlacesInteractor
                    .getFirebaseDataUserPlaces(user.regionId, user.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        var exist: Boolean
                        for (h in it.children) {
                            for (meeting in paperMeetingsInteractor.all()) {
                                if (meeting.placeId.equals(h.key)) {
                                    exist = false
                                    for (i in 0..tempMeetingList.size - 1) {
                                        if (meeting.id.equals(tempMeetingList.get(i).id)) {
                                            exist = true
                                            if (tempMeetingList.get(i).label == null)
                                                tempMeetingList.get(i).label = "Host"
                                            else
                                                tempMeetingList.get(i).label = tempMeetingList.get(i).label + " Host"
                                        }
                                    }
                                    if (!exist) {
                                        if (meeting.creatorId.equals(user.id))
                                            meeting.label = "Admin Host"
                                        else
                                            meeting.label = "Host"
                                        if (search.isEmpty())
                                            tempMeetingList.add(meeting)
                                        else
                                            if (meeting.title.contains(search))
                                                tempMeetingList.add(meeting)
                                    }

                                }
                            }
                        }
                        tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length - 2, it.date.length) }, { it.date.substring(it.date.length - 5, it.date.length - 3) }, { it.date.substring(it.date.length - 8, it.date.length - 6) }, { it.date.substring(0, it.date.lastIndexOf("_")) })))
                        setListTopics(tempMeetingList)
                        view?.setData(tempMeetingList)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.meetingsErrorLoading)
                    },{
                        view?.showProgress(false)
                        tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length - 2, it.date.length) }, { it.date.substring(it.date.length - 5, it.date.length - 3) }, { it.date.substring(it.date.length - 8, it.date.length - 6) }, { it.date.substring(0, it.date.lastIndexOf("_")) })))
                        setListTopics(tempMeetingList)
                        view?.setData(tempMeetingList)
                    }))
        }
    }

    fun getGroupMeetings() {
        view?.showProgress(true)
        var tempMeetingList = arrayListOf<Meeting>()
        compositeDisposable.add(getGroupMeetingsInteractor
                .getFirebaseDataGroupMeetings(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    for (h in it.children) {
                        for (meeting in paperMeetingsInteractor.all()) {
                            if (meeting.id.equals(h.key)) {
                                if (search.isEmpty())
                                    tempMeetingList.add(meeting)
                                else
                                    if (meeting.title.contains(search))
                                        tempMeetingList.add(meeting)
                            }
                        }
                    }
                    tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length - 2, it.date.length) }, { it.date.substring(it.date.length - 5, it.date.length - 3) }, { it.date.substring(it.date.length - 8, it.date.length - 6) }, { it.date.substring(0, it.date.lastIndexOf("_")) })))
                    setListTopics(tempMeetingList)
                    view?.setData(tempMeetingList)
                    view?.showProgress(false)
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.meetingsErrorLoading)
                }))
    }

    fun getPlaceMeetings() {
        view?.showProgress(true)
        var tempMeetingList = arrayListOf<Meeting>()
        compositeDisposable.add(getPlaceMeetingsInteractor
                .getFirebaseDataPlaceMeetings(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    for (h in it.children) {
                        for (meeting in paperMeetingsInteractor.all()) {
                            if (meeting.id.equals(h.key)) {
                                if (search.isEmpty())
                                    tempMeetingList.add(meeting)
                                else
                                    if (meeting.title.contains(search))
                                        tempMeetingList.add(meeting)
                            }
                        }
                    }
                    tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length - 2, it.date.length) }, { it.date.substring(it.date.length - 5, it.date.length - 3) }, { it.date.substring(it.date.length - 8, it.date.length - 6) }, { it.date.substring(0, it.date.lastIndexOf("_")) })))
                    setListTopics(tempMeetingList)
                    view?.setData(tempMeetingList)
                    view?.showProgress(false)
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.meetingsErrorLoading)
                }))
    }

    override fun removeMeeting(meetingId: String) {
        getUserProfile()?.let { user ->
            removeMeetingInteractor
                    .removeFirebaseDataMeeting(user.regionId, meetingId)
                    .subscribe({
                        clearTopicInteractor.clearFCMtopic(meetingId)
                        paperMeetingsInteractor.remove(meetingId)
                        view?.showSuccess(R.string.meetingsSuccessRemoving)
                    }, {
                        view?.showError(R.string.meetingsErrorRemoving)
                    })
        }
    }
}