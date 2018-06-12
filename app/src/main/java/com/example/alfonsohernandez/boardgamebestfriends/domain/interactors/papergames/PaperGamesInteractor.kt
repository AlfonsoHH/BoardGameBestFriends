package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game

interface PaperGamesInteractor {
    fun all(): ArrayList<Game>

    fun get(id: String): Game?

    fun add(value: Game)

    fun addAll(values: ArrayList<Game>)

    fun update(value: Game)

    fun remove(id: String)

    fun clear()
}