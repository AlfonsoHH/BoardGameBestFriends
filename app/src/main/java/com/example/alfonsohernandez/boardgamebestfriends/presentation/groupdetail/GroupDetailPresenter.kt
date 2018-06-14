package com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.AddGroupToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetGroupUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserFromMailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePushPresenter
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Member
import javax.inject.Inject

class GroupDetailPresenter @Inject constructor(private val fcmHandler: FCMHandler,
                                               private val getUserProfileInteractor: GetUserProfileInteractor,
                                               private val paperGroupsInteractor: PaperGroupsInteractor,
                                               private val addGroupToUserInteractor: AddGroupToUserInteractor,
                                               private val getSingleUserFromMailInteractor: GetSingleUserFromMailInteractor,
                                               private val getGroupUsersInteractor: GetGroupUsersInteractor,
                                               private val getSingleUserInteractor: GetSingleUserInteractor,
                                               private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor
                                                ): GroupDetailContract.Presenter, BasePushPresenter<GroupDetailContract.View>() {

    private val TAG = "GroupDetailPresenter"
    var groupId = ""
    var membersList = arrayListOf<User>()

    fun setView(view: GroupDetailContract.View?, groupId: String) {
        this.view = view
        this.groupId = groupId
        if(!groupId.equals("")) {
            getGroupData(groupId)
            getMembersData(groupId)
            firebaseEvent("Showing a Group", TAG)
        }
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

    fun getGroupData(groupId: String) {
        paperGroupsInteractor.get(groupId)?.let {
            view?.setGroupData(it)
        }
    }

    fun getMembersData(groupId: String) {
        view?.showProgress(true)
        getGroupUsersInteractor
                .getFirebaseDataGroupUsers(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    for (h in it.children) {
                        getMemberData(h.key)
                    }
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.groupDetailErrorMembers)
                })
    }

    fun getMemberData(memberId: String) {
        view?.showProgress(true)
        getSingleUserInteractor
                .getFirebaseDataSingleUser(memberId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    val actualUser = it.getValue(User::class.java)
                    if(actualUser != null) {
                        membersList.add(actualUser)
                        view?.setFriendData(actualUser)
                    }
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.groupDetailErrorMembers)
                })
    }

    override fun getFriendFromMailData(email: String) {
        view?.showProgress(true)
        getSingleUserFromMailInteractor
                .getFirebaseDataSingleUserFromMail(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    var alreadyExist = false
                    for(member in membersList){
                        if(member.email.equals(email))
                            alreadyExist = true
                    }
                    if (!alreadyExist) {
                        var actualUser = User()
                        for(h in it.children) {
                            actualUser = h.getValue(User::class.java)!!
                        }
                        saveNewMember(actualUser,groupId)
                    }else{
                        view?.showError(R.string.groupDetailErrorAlready)
                    }
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.addGroupErrorBuddy)
                })
    }

    override fun saveNewMember(user: User, groupId: String) {
        addGroupToUserInteractor.addFirebaseDataGroupToUser(user.id,groupId).subscribe({
            view?.showSuccess(R.string.groupDetailSuccessNewMember)
            view?.setFriendData(user)
        },{
            view?.showError(R.string.groupDetailErrorNewMember)
        })
    }

}