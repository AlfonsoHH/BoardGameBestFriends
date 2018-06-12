package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic

import com.example.alfonsohernandez.boardgamebestfriends.push.FCMtopic

class SetTopicInteractorImpl (private val fcMtopic: FCMtopic): SetTopicInteractor {
    override fun setFCMtopic(topic: String) {
        fcMtopic.setTopic(topic)
    }
}