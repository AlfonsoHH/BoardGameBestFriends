package com.example.alfonsohernandez.boardgamebestfriends.storage.database

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.DatabaseItem
import io.paperdb.Paper

class PaperDatabase<T : DatabaseItem>(private val clazz: Class<T>) : Database<T> {
    companion object {
        fun <T : DatabaseItem> getDatabase(clazz: Class<T>): PaperDatabase<T> {
            return PaperDatabase(clazz)
        }
    }

    /**
     * Reads all the keys from our paperbook and returns the results as an arraylist
     */
    override fun all(): ArrayList<T> {
        val results = arrayListOf<T>()

        val keys = Paper.book(clazz.simpleName).allKeys

        for (key in keys) {
            val result = Paper.book(clazz.simpleName).read<T>(key, null)

            if (result != null && result is T) {
                results.add(result)
            }

        }

        return results
    }


    override fun get(id: Int): T? {
        return Paper.book(clazz.simpleName).read<T>(id.toString(), null)
    }

    override fun addAll(values: ArrayList<T>) {
        for (value in values) {
            add(value)
        }
    }

    override fun add(value: T) {
        Paper.book(clazz.simpleName).write<T>(value.id.toString(), value)
    }

    override fun update(value: T) {
        Paper.book(clazz.simpleName).write<T>(value.id.toString(), value)
    }

    override fun remove(id: Int) {
        Paper.book(clazz.simpleName).delete(id.toString())
    }

    override fun clear() {
        Paper.book(clazz.simpleName).destroy()
    }
}