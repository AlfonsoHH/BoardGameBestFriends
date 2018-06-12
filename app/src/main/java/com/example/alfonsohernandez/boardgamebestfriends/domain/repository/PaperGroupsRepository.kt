package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.StringDatabase

class PaperGroupsRepository(private val database: StringDatabase<Group>) {

    fun all(): ArrayList<Group> {
        return database.all()
    }

    fun get(id: String): Group? {
        return database.get(id)
    }

    fun add(value: Group) {
        return database.add(value)
    }

    fun addAll(values: ArrayList<Group>) {
        database.addAll(values)
    }

    fun update(value: Group) {
        return database.update(value)
    }

    fun remove(id: String) {
        return database.remove(id)
    }

    fun clear() {
        database.clear()
    }
}