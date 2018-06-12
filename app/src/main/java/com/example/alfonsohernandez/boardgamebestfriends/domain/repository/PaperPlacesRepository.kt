package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.StringDatabase

class PaperPlacesRepository(private val database: StringDatabase<Place>) {

    fun all(): ArrayList<Place> {
        return database.all()
    }

    fun get(id: String): Place? {
        return database.get(id)
    }

    fun add(value: Place) {
        return database.add(value)
    }

    fun addAll(values: ArrayList<Place>) {
        database.addAll(values)
    }

    fun update(value: Place) {
        return database.update(value)
    }

    fun remove(id: String) {
        return database.remove(id)
    }

    fun clear() {
        database.clear()
    }
}