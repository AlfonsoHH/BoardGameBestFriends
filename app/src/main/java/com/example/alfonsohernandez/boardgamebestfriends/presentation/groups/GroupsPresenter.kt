package com.example.alfonsohernandez.boardgamebestfriends.presentation.groups

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetUserGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.RemoveGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.RemoveGroupToUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class GroupsPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                          private val getUserGroupsInteractor: GetUserGroupsInteractor,
                                          private val getSingleGroupInteractor: GetSingleGroupInteractor,
                                          private val removeGroupInteractor: RemoveGroupInteractor,
                                          private val removeGroupToUserInteractor: RemoveGroupToUserInteractor): GroupsContract.Presenter {

    private var view: GroupsContract.View? = null

    private val TAG = "GroupsPresenter"

    fun setView(view: GroupsContract.View?) {
        this.view = view
        getGroupsData()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getGroupsData() {
        view?.showProgressBar(true)
        getUserGroupsInteractor
                .getFirebaseDataUserGroups(getUserProfile()!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    for (h in it.children) {
                        getSingleGroupData(h.key)
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun getSingleGroupData(groupId: String) {
        view?.showProgressBar(true)
        getSingleGroupInteractor
                .getFirebaseDataSingleGroup(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    view?.setData(it.getValue(Group::class.java)!!)
                },{
                    view?.showProgressBar(false)
                    view?.showErrorLoading()
                })
    }

    override fun removeGroup(group: Group) {
        removeGroupInteractor
                .removeFirebaseDataGroup(group.id)
                .subscribe({
                    view?.successRemovingGroup()
                    view?.removeGroup(group)
                },{
                    view?.showErrorRemovingGroup()
                })

    }

    override fun removeUserFromGroup(group: Group) {
        removeGroupToUserInteractor
                .removeFirebaseDataGroupToUser(getUserProfile()!!.id,group.id)
                .subscribe({
                    view?.successRemovingUser()
                    view?.removeGroup(group)
                },{
                    view?.showErrorRemovingUser()
                })
    }
}