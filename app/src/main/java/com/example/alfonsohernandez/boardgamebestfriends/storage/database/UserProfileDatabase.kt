package com.example.alfonsohernandez.boardgamebestfriends.storage.preferences

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.paperdb.Paper

class UserProfileDatabase(context: Context) {

    private val TAG = "UserProfileDatabase"
    private val KEY_USER_PROFILE = "KEY_USER_PROFILE"

    init {
        Paper.init(context)
    }

    fun get(): User? {
        return Paper.book().read(KEY_USER_PROFILE, null)
    }

    fun set(value: User?) {
        Paper.book().write(KEY_USER_PROFILE, value)
    }

    fun clear() {
        Paper.book().delete(KEY_USER_PROFILE)
    }

}