package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group

interface PaperGroupsInteractor {
    fun all(): ArrayList<Group>

    fun get(id: String): Group?

    fun add(value: Group)

    fun addAll(values: ArrayList<Group>)

    fun update(value: Group)

    fun remove(id: String)

    fun clear()
}