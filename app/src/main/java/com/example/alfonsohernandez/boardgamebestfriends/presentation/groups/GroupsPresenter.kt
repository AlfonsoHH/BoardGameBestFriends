package com.example.alfonsohernandez.boardgamebestfriends.presentation.groups

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetUserGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.RemoveGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.RemoveGroupToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.ClearTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.SetTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePushPresenter
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class GroupsPresenter @Inject constructor(private val fcmHandler: FCMHandler,
                                          private val getUserProfileInteractor: GetUserProfileInteractor,
                                          private val paperGroupsInteractor: PaperGroupsInteractor,
                                          private val paperMeetingsInteractor: PaperMeetingsInteractor,
                                          private val getUserGroupsInteractor: GetUserGroupsInteractor,
                                          private val getSingleGroupInteractor: GetSingleGroupInteractor,
                                          private val removeGroupInteractor: RemoveGroupInteractor,
                                          private val removeGroupToUserInteractor: RemoveGroupToUserInteractor,
                                          private val SetTopicInteractor: SetTopicInteractor,
                                          private val ClearTopicInteractor: ClearTopicInteractor,
                                          private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor
                                        ): GroupsContract.Presenter,
                                            BasePushPresenter<GroupsContract.View>() {

    private val TAG = "GroupsPresenter"

    var adapterList = arrayListOf<Group>()

    var compositeDisposable = CompositeDisposable()

    fun unsetViewFragment(){
        this.view = null
        compositeDisposable.clear()
    }

    fun unsetView(){
        this.view = null
        compositeDisposable.dispose()
    }

    fun setView(view: GroupsContract.View?) {
        this.view = view
        getGroupsDataRx()
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

    fun getGroupsDataRx() {
        adapterList = arrayListOf()
        getUserProfile()?.let {usr ->
            view?.showProgress(true)
            compositeDisposable.add(getUserGroupsInteractor
                    .getFirebaseDataUserGroups(usr.id).toObservable()
                    .flatMapIterable { it.children }
                    .flatMap { getSingleGroupDataRx(it.key) }
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.showProgress(false)
                        paperGroupsInteractor.clear()
                        paperGroupsInteractor.addAll(adapterList)
                        var groupList = paperGroupsInteractor.all()
                        groupList = ArrayList(groupList.sortedBy { it.title })
                        view?.setData(groupList)
                    }, {
                        view?.showProgress(false)
                        view?.showError(R.string.groupsErrorLoading)
                    }))
        }
    }

    fun getSingleGroupDataRx(groupId: String): Observable<Boolean> {
        return Observable.create { obs ->
            getSingleGroupInteractor
                    .getFirebaseDataSingleGroup(groupId)
                    .map {
                        SetTopicInteractor.setFCMtopic(groupId)
                        val group = it.getValue(Group::class.java)
                        group?.let { grp ->
                            var meetingsCount = 0
                            for (meeting in paperMeetingsInteractor.all()) {
                                if (meeting.groupId.equals(grp.id))
                                    meetingsCount = meetingsCount + 1
                            }
                            grp.meetings = meetingsCount
                            adapterList.add(grp)
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        obs.onNext(true)
                        obs.onComplete() },
                            { obs.onError(it) },
                            { obs.onComplete() })
        }
    }

    override fun updateGroupsFromCache() {
        var groupList = paperGroupsInteractor.all()
        groupList = ArrayList(groupList.sortedBy { it.title })
        view?.setData(groupList)
    }

    override fun removeGroup(group: Group) {
        removeGroupInteractor
                .removeFirebaseDataGroup(group.id)
                .subscribe({
                    ClearTopicInteractor.clearFCMtopic(group.id)
                    view?.showSuccess(R.string.groupsSuccessRemovingGroup)
                    paperGroupsInteractor.remove(group.id)
                    updateGroupsFromCache()
                },{
                    view?.showError(R.string.groupsErrorDeRemoveGroup)
                })

    }

    override fun removeUserFromGroup(group: Group) {
        getUserProfile()?.let { user ->
            removeGroupToUserInteractor
                    .removeFirebaseDataGroupToUser(user.id, group.id)
                    .subscribe({
                        ClearTopicInteractor.clearFCMtopic(group.id)
                        view?.showSuccess(R.string.groupsSuccessRemovingUser)
                        paperGroupsInteractor.remove(group.id)
                        updateGroupsFromCache()
                    }, {
                        view?.showError(R.string.groupsErrorRemovingUser)
                    })
        }
    }
}