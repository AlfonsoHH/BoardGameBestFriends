package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting

interface PaperMeetingsInteractor {
    fun all(): ArrayList<Meeting>

    fun get(id: String): Meeting?

    fun add(value: Meeting)

    fun addAll(values: ArrayList<Meeting>)

    fun update(value: Meeting)

    fun remove(id: String)

    fun clear()
}