package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperGroupsRepository

class PaperGroupsInteractorImpl(private val paperGroupsRepository: PaperGroupsRepository): PaperGroupsInteractor {
    override fun all(): ArrayList<Group> {
        return paperGroupsRepository.all()
    }

    override fun get(id: String): Group? {
        return paperGroupsRepository.get(id)
    }

    override fun add(value: Group) {
        paperGroupsRepository.add(value)
    }

    override fun addAll(values: ArrayList<Group>) {
        paperGroupsRepository.addAll(values)
    }

    override fun update(value: Group) {
        paperGroupsRepository.update(value)
    }

    override fun remove(id: String) {
        paperGroupsRepository.remove(id)
    }

    override fun clear() {
        paperGroupsRepository.clear()
    }
}