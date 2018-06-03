package com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.AddGroupToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetGroupUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Member
import javax.inject.Inject

class GroupDetailPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                               private val addGroupToUserInteractor: AddGroupToUserInteractor,
                                               private val getSingleGroupInteractor: GetSingleGroupInteractor,
                                               private val getRegionInteractor: GetRegionInteractor,
                                               private val getGroupUsersInteractor: GetGroupUsersInteractor,
                                               private val getSingleUserInteractor: GetSingleUserInteractor,
                                               private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GroupDetailContract.Presenter {

    private val TAG = "GroupDetailPresenter"
    var groupId = ""
    var membersList = arrayListOf<User>()

    private var view: GroupDetailContract.View? = null

    fun setView(view: GroupDetailContract.View?, groupId: String) {
        this.view = view
        this.groupId = groupId
        if(!groupId.equals("")) {
            Log.d(TAG,"Inside GroupId")
            getGroupData(groupId)
            getMembersData(groupId)
            firebaseEvent("Showing a Group", TAG)
        }
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getGroupData(groupId: String) {
        view?.showProgressBar(true)
        getSingleGroupInteractor
                .getFirebaseDataSingleGroup(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var group = it.getValue(Group::class.java)
                    view?.setGroupData(group!!)
                    getRegionData(getUserProfile()!!.regionId)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorGroup()
                })
    }

    override fun getRegionData(regionId: String) {
        view?.showProgressBar(true)
        getRegionInteractor
                .getFirebaseDataSingleRegion(regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setRegionData(it.getValue(Region::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorRegion()
                })
    }

    override fun getMembersData(groupId: String) {
        view?.showProgressBar(true)
        getGroupUsersInteractor
                .getFirebaseDataGroupUsers(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    //view?.clearFriendsData()
                    for (h in it.children) {
                        getMemberData(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorMembers()
                })
    }

    override fun getMemberData(memberId: String) {
        view?.showProgressBar(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(memberId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var actualUser = it.getValue(User::class.java)
                    membersList.add(actualUser!!)
                    view?.setFriendData(actualUser!!)
                    Log.d(TAG,actualUser.userName)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorMembers()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    override fun getFriendData(userId: String) {
        view?.showProgressBar(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var alreadyExist = false
                    for(member in membersList){
                        if(member.id.equals(userId))
                            alreadyExist = true
                    }
                    if(!alreadyExist)
                        saveNewMember(it.getValue(User::class.java)!!,groupId)
                    else
                        view?.showErrorAlreadyExist()
                },{
                    view?.showProgressBar(false)
                    view?.showErrorDoesNotExist()
                })
    }

    override fun saveNewMember(user: User, groupId: String) {
        addGroupToUserInteractor.addFirebaseDataGroupToUser(user.id,groupId).subscribe({
            view?.successNewMember()
            view?.setFriendData(user)
        },{
            view?.showErrorNewMember()
        })
    }

}