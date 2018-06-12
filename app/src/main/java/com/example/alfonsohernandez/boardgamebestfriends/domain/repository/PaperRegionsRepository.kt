package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.StringDatabase

class PaperRegionsRepository(private val database: StringDatabase<Region>) {

    fun all(): ArrayList<Region> {
        return database.all()
    }

    fun get(id: String): Region? {
        return database.get(id)
    }

    fun add(value: Region) {
        return database.add(value)
    }

    fun addAll(values: ArrayList<Region>) {
        database.addAll(values)
    }

    fun update(value: Region) {
        return database.update(value)
    }

    fun remove(id: String) {
        return database.remove(id)
    }

    fun clear() {
        database.clear()
    }
}