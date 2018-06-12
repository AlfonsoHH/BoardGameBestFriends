package com.example.alfonsohernandez.boardgamebestfriends.storage.database

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.DatabaseItem
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.DatabaseStringItem


interface Database<T : DatabaseItem> {
    fun all(): ArrayList<T>
    fun get(id: Int): T?
    fun addAll(values: ArrayList<T>)
    fun add(value: T)
    fun update(value: T)
    fun remove(id: Int)
    fun clear()
}

interface StringDatabase<T : DatabaseStringItem> {
    fun all(): ArrayList<T>
    fun get(id: String): T?
    fun addAll(values: ArrayList<T>)
    fun add(value: T)
    fun update(value: T)
    fun remove(id: String)
    fun clear()
}

interface StringSingleDatabase<T> {
    fun get(): T?
    fun add(value: T)
    fun update(value: T)
    fun remove()
    fun clear()
}
