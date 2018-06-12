package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperGamesRepository

class PaperGamesInteractorImpl(private val paperGamesRepository: PaperGamesRepository): PaperGamesInteractor {
    override fun all(): ArrayList<Game> {
        return paperGamesRepository.all()
    }

    override fun get(id: String): Game? {
        return paperGamesRepository.get(id)
    }

    override fun add(value: Game) {
        paperGamesRepository.add(value)
    }

    override fun addAll(values: ArrayList<Game>) {
        paperGamesRepository.addAll(values)
    }

    override fun update(value: Game) {
        paperGamesRepository.update(value)
    }

    override fun remove(id: String) {
        paperGamesRepository.remove(id)
    }

    override fun clear() {
        paperGamesRepository.clear()
    }
}