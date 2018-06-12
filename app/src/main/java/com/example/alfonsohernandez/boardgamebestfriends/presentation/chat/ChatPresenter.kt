package com.example.alfonsohernandez.boardgamebestfriends.presentation.chat

import android.util.Log
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.GetMessagesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.SendMessageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetGroupUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ChatPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                        private val paperGroupsInteractor: PaperGroupsInteractor,
                                        private val getSingleUserInteractor: GetSingleUserInteractor,
                                        private val getGroupUsersInteractor: GetGroupUsersInteractor,
                                        private val getMessagesInteractor: GetMessagesInteractor,
                                        private val sendMessageInteractor: SendMessageInteractor,
                                        private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): ChatContract.Presenter, BasePresenter<ChatContract.View>() {

    private val TAG = "ChatPresenter"
    var groupId = ""

    var hasFinished = false

    var userList = arrayListOf<User>()

    fun setView(view: ChatContract.View?, groupId: String) {
        this.view = view
        this.groupId = groupId
        firebaseEvent("Showing chat",TAG)
        getGroupUsers(this.groupId)
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

    fun getGroupUsers(groupId: String){
        getGroupUsersInteractor
                .getFirebaseDataGroupUsers(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    for(h in it.children){
                        getSingleUser(h.key)
                    }
                    hasFinished = true
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.chatErrorLoadingChat)
                })
    }

    fun getSingleUser(userId: String){
        getSingleUserInteractor
                .getFirebaseDataSingleUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    val actualUser = it.getValue(User::class.java)
                    actualUser?.let {actUsr ->
                        userList.add(actUsr)
                    }
                    if(hasFinished)
                        paperGroupsInteractor.get(groupId)?.let {group ->
                            loadFirebaseChat(group)
                        }
                },{
                    view?.showProgress(false)
                    view?.showError(R.string.chatErrorLoadingGroupData)
                })
    }

    override fun loadFirebaseChat(group: Group) {
        getMessagesInteractor
                .getFirebaseDataMessages(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    val messageList = arrayListOf<Message>()
                    for (h in it.children) {
                        val message = h.getValue(Message::class.java)
                        message?.let { msg ->
                            for (user in userList) {
                                if (user.id.equals(msg.userId)) {
                                    msg.userName = user.userName
                                    msg.userPhoto = user.photo
                                    msg.userProvider = user.service
                                    msg.user = false
                                    if (user.id.equals(getUserProfile()?.id))
                                        msg.user = true
                                }
                            }
                            messageList.add(msg)
                        }
                    }
                    view?.setData(group, messageList)
                }, {
                    view?.showProgress(false)
                    view?.showError(R.string.chatErrorLoadingChat)
                })
    }

    override fun sendMessage(message: Message) {
        sendMessageInteractor
                .sendFirebaseDataMessage(groupId,message)
                .subscribe({
                    Timber.d(TAG + " " + message.text)
                },{
                    view?.showError(R.string.chatErrorAddingMessage)
                })
    }

}