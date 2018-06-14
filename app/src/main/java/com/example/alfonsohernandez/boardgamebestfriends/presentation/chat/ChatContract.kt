package com.example.alfonsohernandez.boardgamebestfriends.presentation.chat

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

interface ChatContract {

    interface View: BaseNotificationView {

        fun setData(group: Group, messages: ArrayList<Message>)
        fun setupRecycler()

    }

    interface Presenter{

        fun loadFirebaseChat(group: Group)
        fun sendMessage(message: Message)

    }

}