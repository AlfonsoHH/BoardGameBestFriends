package com.example.alfonsohernandez.boardgamebestfriends.storage.database

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.DatabaseStringItem
import io.paperdb.Paper
import timber.log.Timber

class NamedPaperDatabase<T : DatabaseStringItem>(private val name: String) : StringDatabase<T> {

    /**
     * Reads all the keys from our paperbook and returns the results as an arraylist
     */
    override fun all(): ArrayList<T> {
        val results = arrayListOf<T>()
        try {
            val keys = Paper.book(name).allKeys
            keys
                    .map { Paper.book(name).read<T>(it, null) }
                    .filterTo(results) { it != null && it is T }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return results
    }

    override fun get(id: String): T? {
        return Paper.book(name).read<T>(id, null)
    }

    @Synchronized
    override fun addAll(values: ArrayList<T>) {
        for (value in values) {
            add(value)
        }
    }

    @Synchronized
    override fun add(value: T) {
        try {
            Paper.book(name).write<T>(value.id, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    override fun update(value: T) {
        try {
            Paper.book(name).write<T>(value.id, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    override fun remove(id: String) {
        Paper.book(name).delete(id)
    }

    @Synchronized
    override fun clear() {
        Paper.book(name).destroy()
    }
}