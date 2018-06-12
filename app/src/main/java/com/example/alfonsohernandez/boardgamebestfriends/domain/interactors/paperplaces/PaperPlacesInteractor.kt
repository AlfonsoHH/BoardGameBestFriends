package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place

interface PaperPlacesInteractor {
    fun all(): ArrayList<Place>

    fun get(id: String): Place?

    fun add(value: Place)

    fun addAll(values: ArrayList<Place>)

    fun update(value: Place)

    fun remove(id: String)

    fun clear()
}