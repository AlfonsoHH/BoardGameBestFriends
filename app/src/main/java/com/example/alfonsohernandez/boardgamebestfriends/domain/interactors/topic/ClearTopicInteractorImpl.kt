package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic

import com.example.alfonsohernandez.boardgamebestfriends.push.FCMtopic

class ClearTopicInteractorImpl (private val fcMtopic: FCMtopic): ClearTopicInteractor{
    override fun clearFCMtopic(topic: String) {
        fcMtopic.clearTopic(topic)
    }
}