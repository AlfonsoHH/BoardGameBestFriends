package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperMeetingsRepository

class PaperMeetingsInteractorImpl(private val paperMeetingsRepository: PaperMeetingsRepository): PaperMeetingsInteractor {
    override fun all(): ArrayList<Meeting> {
        return paperMeetingsRepository.all()
    }

    override fun get(id: String): Meeting? {
        return paperMeetingsRepository.get(id)
    }

    override fun add(value: Meeting) {
        paperMeetingsRepository.add(value)
    }

    override fun addAll(values: ArrayList<Meeting>) {
        paperMeetingsRepository.addAll(values)
    }

    override fun update(value: Meeting) {
        paperMeetingsRepository.update(value)
    }

    override fun remove(id: String) {
        paperMeetingsRepository.remove(id)
    }

    override fun clear() {
        paperMeetingsRepository.clear()
    }
}