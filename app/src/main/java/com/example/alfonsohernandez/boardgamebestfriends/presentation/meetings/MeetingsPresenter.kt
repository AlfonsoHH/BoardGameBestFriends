package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetUserGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetUserPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class MeetingsPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                            private val getOpenMeetingsInteractor: GetOpenMeetingsInteractor,
                                            private val getUserMeetingsInteractor: GetUserMeetingsInteractor,
                                            private val getUserGroupsInteractor: GetUserGroupsInteractor,
                                            private val getUserPlacesInteractor: GetUserPlacesInteractor,
                                            private val getGroupMeetingsInteractor: GetGroupMeetingsInteractor,
                                            private val getPlaceMeetingsInteractor: GetPlaceMeetingsInteractor,
                                            private val removeMeetingInteractor: RemoveMeetingInteractor): MeetingsContract.Presenter {

    private var view: MeetingsContract.View? = null
    var kind = ""
    var search = ""

    var allMeetings = arrayListOf<Meeting>()

    private val TAG = "MeetingsPresenter"

    fun setView(view: MeetingsContract.View?, kind: String) {
        this.view = view
        this.kind = kind
        dataChooser()
    }

    override fun dataChooser() {
        if(view != null) {
            search = view?.getSearchData()!!
            if (kind!!.contains("buddy-"))
                loadAllMeetings("buddy-")
            else if (kind!!.contains("group-"))
                loadAllMeetings("group-")
            else if (kind!!.contains("place-"))
                loadAllMeetings("place-")
            else
                getMeetingsData()
        }
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    fun loadAllMeetings(kind: String){
        view?.showProgressBar(true)
        getOpenMeetingsInteractor
                .getFirebaseDataOpenMeetings(getUserProfile()!!.regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    allMeetings.clear()
                    for (h in it.children) {
                        allMeetings.add(h.getValue(Meeting::class.java)!!)
                    }
                    if(kind.equals("buddy-"))
                        getUserMeetings()
                    else if(kind.equals("group-"))
                        getGroupMeetings()
                    else
                        getPlaceMeetings()
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getMeetingsData() {
        view?.showProgressBar(true)
        getOpenMeetingsInteractor
                .getFirebaseDataOpenMeetings(getUserProfile()!!.regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var tempMeetingList = arrayListOf<Meeting>()
                    allMeetings.clear()
                    for (h in it.children) {
                        var actualMeeting = h.getValue(Meeting::class.java!!)
                        allMeetings.add(actualMeeting!!)
                        if(search!!.isEmpty() && actualMeeting.groupId.equals("open") && actualMeeting.vacants > 0) {
                            actualMeeting.label = "Open"
                            tempMeetingList.add(actualMeeting!!)
                        }else {
                            if (actualMeeting!!.title.contains(search) && actualMeeting.groupId.equals("open") && actualMeeting.vacants > 0)
                                tempMeetingList.add(actualMeeting!!)
                        }
                    }
                    getUserGroups(tempMeetingList)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    fun getUserGroups(tempMeetingList: ArrayList<Meeting>){
        view?.showProgressBar(true)
        var tempMeetingList = tempMeetingList
        getUserGroupsInteractor
                .getFirebaseDataUserGroups(getUserProfile()!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    //var exist: Boolean
                    for (h in it.children) {
                        Log.d(TAG,h.key)
                        for(meeting in allMeetings){
                            if(meeting.groupId.equals(h.key)){
                                //exist = false
                                //for(temp in tempMeetingList) {
                                //    if(meeting.id.equals(temp.id)) {
                                //        exist = true
                                //    }
                                //}
                                //if(!exist) {
                                    meeting.label = "Group"
                                    if (search!!.isEmpty())
                                        tempMeetingList.add(meeting)
                                    else
                                        if (meeting!!.title.contains(search))
                                            tempMeetingList.add(meeting)
                                //}
                            }
                        }
                    }
                    tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length-2,it.date.length) }, { it.date.substring(it.date.length-5,it.date.length-3) }, { it.date.substring(it.date.length-8,it.date.length-6) }, { it.date.substring(0,it.date.lastIndexOf("_")) })))
                    view?.clearData()
                    view?.setData(tempMeetingList)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    fun getUserPlaces(tempMeetingList: ArrayList<Meeting>){
        var tempMeetingList = tempMeetingList
        view?.showProgressBar(true)
        getUserPlacesInteractor
                .getFirebaseDataUserPlaces(getUserProfile()!!.regionId,getUserProfile()!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var exist: Boolean
                    for (h in it.children) {
                        for(meeting in allMeetings){
                            if(meeting.placeId.equals(h.key)) {
                                exist = false
                                for(i in 0..tempMeetingList.size-1) {
                                    if(meeting.id.equals(tempMeetingList.get(i).id)) {
                                        exist = true
                                        if(tempMeetingList.get(i).label == null)
                                            tempMeetingList.get(i).label = "Host"
                                        else
                                            tempMeetingList.get(i).label = tempMeetingList.get(i).label + " Host"
                                    }
                                }
                                if(!exist){
                                    if(meeting.creatorId.equals(getUserProfile()!!.id))
                                        meeting.label = "Admin Host"
                                    else
                                        meeting.label = "Host"
                                    if(search!!.isEmpty())
                                        tempMeetingList.add(meeting)
                                    else
                                        if(meeting!!.title.contains(search))
                                            tempMeetingList.add(meeting)
                                }

                            }
                        }
                    }
                    tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length-2,it.date.length) }, { it.date.substring(it.date.length-5,it.date.length-3) }, { it.date.substring(it.date.length-8,it.date.length-6) }, { it.date.substring(0,it.date.lastIndexOf("_")) })))
                    view?.clearData()
                    view?.setData(tempMeetingList)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getUserMeetings() {
        view?.showProgressBar(true)
        getUserMeetingsInteractor
                .getFirebaseDataUserMeetings(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.clearData()
                    var tempMeetingList = arrayListOf<Meeting>()
                    for (h in it.children) {
                        for(meeting in allMeetings){
                            if(meeting.id.equals(h.key)) {
                                if(meeting.creatorId.equals(getUserProfile()!!.id) && h.getValue(Boolean::class.java)!!)
                                    meeting.label = "Admin Playing"
                                else if (meeting.creatorId.equals(getUserProfile()!!.id))
                                    meeting.label = "Admin"
                                else if(h.getValue(Boolean::class.java)!!)
                                    meeting.label = "Playing"
                                if(search!!.isEmpty())
                                    tempMeetingList.add(meeting)
                                else
                                    if(meeting!!.title.contains(search))
                                        tempMeetingList.add(meeting)
                            }
                        }
                    }
                    getUserPlaces(tempMeetingList)
                    view?.showProgressBar(false)
                }, {
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getGroupMeetings() {
        view?.showProgressBar(true)
        var tempMeetingList = arrayListOf<Meeting>()
        getGroupMeetingsInteractor
                .getFirebaseDataGroupMeetings(kind.substring(6, kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    for (h in it.children) {
                        for(meeting in allMeetings){
                            if(meeting.id.equals(h.key)) {
                                if (search!!.isEmpty())
                                    tempMeetingList.add(meeting)
                                else
                                    if (meeting!!.title.contains(search))
                                        tempMeetingList.add(meeting)
                            }
                        }
                    }
                    tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length-2,it.date.length) }, { it.date.substring(it.date.length-5,it.date.length-3) }, { it.date.substring(it.date.length-8,it.date.length-6) }, { it.date.substring(0,it.date.lastIndexOf("_")) })))
                    view?.clearData()
                    view?.setData(tempMeetingList)
                    view?.showProgressBar(false)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getPlaceMeetings() {
        view?.showProgressBar(true)
        var tempMeetingList = arrayListOf<Meeting>()
        getPlaceMeetingsInteractor
                .getFirebaseDataPlaceMeetings(kind.substring(6,kind.length))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    for (h in it.children) {
                        for(meeting in allMeetings){
                            if(meeting.id.equals(h.key)) {
                                if (search!!.isEmpty())
                                    tempMeetingList.add(meeting)
                                else
                                    if (meeting!!.title.contains(search))
                                        tempMeetingList.add(meeting)
                            }
                        }
                    }
                    tempMeetingList = ArrayList(tempMeetingList.sortedWith(compareBy({ it.date.substring(it.date.length-2,it.date.length) }, { it.date.substring(it.date.length-5,it.date.length-3) }, { it.date.substring(it.date.length-8,it.date.length-6) }, { it.date.substring(0,it.date.lastIndexOf("_")) })))
                    view?.clearData()
                    view?.setData(tempMeetingList)
                    view?.showProgressBar(false)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun removeMeeting(meetingId: String) {
        removeMeetingInteractor
                .removeFirebaseDataMeeting(getUserProfile()!!.regionId,meetingId)
                .subscribe({
                    view?.successRemoving()
                },{
                    view?.showErrorRemoving()
                })
    }
}