package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.ChatRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Flowable

class GetMessagesInteractorImpl (private val chatRepository: ChatRepository): GetMessagesInteractor {
    override fun getFirebaseDataMessages(groupId: String): Flowable<DataSnapshot> {
        return chatRepository.getMessagesRx(groupId)
    }
}