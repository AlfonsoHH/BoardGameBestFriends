package com.example.alfonsohernandez.boardgamebestfriends.storage.database

import io.paperdb.Paper

class StringSinglePaperDatabase<T>(private val name: String) :
        StringSingleDatabase<T> {

    private val KEY = "SINGLE"

    override fun get(): T? {
        return Paper.book(name).read<T>(KEY, null)
    }

    override fun add(value: T) {
        Paper.book(name).write<T>(KEY, value)
    }

    override fun update(value: T) {
        Paper.book(name).write<T>(KEY, value)
    }

    override fun remove() {
        Paper.book(name).delete(KEY)
    }

    override fun clear() {
        Paper.book(name).destroy()
    }
}