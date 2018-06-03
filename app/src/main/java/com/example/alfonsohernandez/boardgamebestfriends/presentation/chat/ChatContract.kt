package com.example.alfonsohernandez.boardgamebestfriends.presentation.chat

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

interface ChatContract {

    interface View{

        fun setData(group: Group, messages: ArrayList<Message>)
        fun showErrorAdding()
        fun showErrorLoading()
        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter{

        fun loadFirebaseChat()
        fun sendMessage(message: Message)

        fun getUserProfile(): User?

        fun firebaseEvent(id: String, activityName: String)

    }

}