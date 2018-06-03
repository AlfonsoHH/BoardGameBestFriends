package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.ChatRepository
import io.reactivex.Completable

class SendMessageInteractorImpl (private val chatRepository: ChatRepository): SendMessageInteractor {
    override fun sendFirebaseDataMessage(groupId: String, message: Message): Completable {
        return Completable.create {
            try {
                chatRepository.sendMessage(groupId,message)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}