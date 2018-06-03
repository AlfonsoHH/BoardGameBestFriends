package com.example.alfonsohernandez.boardgamebestfriends.presentation.chat

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.GetMessagesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.SendMessageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.GetSingleGroupInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import javax.inject.Inject

class ChatPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                        private val getMessagesInteractor: GetMessagesInteractor,
                                        private val sendMessageInteractor: SendMessageInteractor,
                                        private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): ChatContract.Presenter {

    private val TAG = "ChatPresenter"

    private var view: ChatContract.View? = null
    var groupId = ""

    fun setView(view: ChatContract.View?, groupId: String) {
        this.view = view
        this.groupId = groupId
        firebaseEvent("Showing chat",TAG)
        loadFirebaseChat()
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun loadFirebaseChat() {
        getMessagesInteractor
                .getFirebaseDataMessages(groupId)
    }

    override fun sendMessage(message: Message) {
        sendMessageInteractor
                .sendFirebaseDataMessage(groupId,message)
                .subscribe({},{
                    view?.showErrorAdding()
                })
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }

}