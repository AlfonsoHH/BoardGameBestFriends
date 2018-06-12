package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.StringDatabase

class PaperGamesRepository(private val database: StringDatabase<Game>) {

    fun all(): ArrayList<Game> {
        return database.all()
    }

    fun get(id: String): Game? {
        return database.get(id)
    }

    fun add(value: Game) {
        return database.add(value)
    }

    fun addAll(values: ArrayList<Game>) {
        database.addAll(values)
    }

    fun update(value: Game) {
        return database.update(value)
    }

    fun remove(id: String) {
        return database.remove(id)
    }

    fun clear() {
        database.clear()
    }
}