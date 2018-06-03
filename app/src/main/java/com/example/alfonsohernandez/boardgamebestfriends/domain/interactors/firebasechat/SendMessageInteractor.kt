package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import io.reactivex.Completable

interface SendMessageInteractor {
    fun sendFirebaseDataMessage(groupId: String, message: Message): Completable
}