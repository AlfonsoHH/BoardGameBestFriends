package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region

interface PaperRegionsInteractor {
    fun all(): ArrayList<Region>

    fun get(id: String): Region?

    fun add(value: Region)

    fun addAll(values: ArrayList<Region>)

    fun update(value: Region)

    fun remove(id: String)

    fun clear()
}