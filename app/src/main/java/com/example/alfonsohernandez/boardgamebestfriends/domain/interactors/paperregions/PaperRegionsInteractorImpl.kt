package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.PaperRegionsRepository

class PaperRegionsInteractorImpl(private val paperRegionsRepository: PaperRegionsRepository): PaperRegionsInteractor {
    override fun all(): ArrayList<Region> {
        return paperRegionsRepository.all()
    }

    override fun get(id: String): Region? {
        return paperRegionsRepository.get(id)
    }

    override fun add(value: Region) {
        paperRegionsRepository.add(value)
    }

    override fun addAll(values: ArrayList<Region>) {
        paperRegionsRepository.addAll(values)
    }

    override fun update(value: Region) {
        paperRegionsRepository.update(value)
    }

    override fun remove(id: String) {
        paperRegionsRepository.remove(id)
    }

    override fun clear() {
        paperRegionsRepository.clear()
    }
}