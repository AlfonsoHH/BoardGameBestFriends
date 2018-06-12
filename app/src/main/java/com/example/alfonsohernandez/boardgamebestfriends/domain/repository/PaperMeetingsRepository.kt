package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.StringDatabase

class PaperMeetingsRepository(private val database: StringDatabase<Meeting>) {

    fun all(): ArrayList<Meeting> {
        return database.all()
    }

    fun get(id: String): Meeting? {
        return database.get(id)
    }

    fun add(value: Meeting) {
        return database.add(value)
    }

    fun addAll(values: ArrayList<Meeting>) {
        database.addAll(values)
    }

    fun update(value: Meeting) {
        return database.update(value)
    }

    fun remove(id: String) {
        return database.remove(id)
    }

    fun clear() {
        database.clear()
    }
}