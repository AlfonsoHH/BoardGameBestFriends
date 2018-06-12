package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperPlacesRepository

class PaperPlacesInteractorImpl(private val paperPlacesRepository: PaperPlacesRepository): PaperPlacesInteractor {
    override fun all(): ArrayList<Place> {
        return paperPlacesRepository.all()
    }

    override fun get(id: String): Place? {
        return paperPlacesRepository.get(id)
    }

    override fun add(value: Place) {
        paperPlacesRepository.add(value)
    }

    override fun addAll(values: ArrayList<Place>) {
        paperPlacesRepository.addAll(values)
    }

    override fun update(value: Place) {
        paperPlacesRepository.update(value)
    }

    override fun remove(id: String) {
        paperPlacesRepository.remove(id)
    }

    override fun clear() {
        paperPlacesRepository.clear()
    }
}