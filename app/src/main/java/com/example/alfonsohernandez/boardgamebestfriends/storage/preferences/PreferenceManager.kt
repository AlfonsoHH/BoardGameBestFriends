package com.example.alfonsohernandez.boardgamebestfriends.storage.database

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

class PreferencesManager(context: Context) {

    lateinit var pref: SharedPreferences

    init {
        pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    fun getPreferencesString(parameter: String):String{
        return pref.getString(parameter,"")
    }

    fun getPreferencesInt(parameter: String):Int{
        return pref.getInt(parameter,0)
    }

    fun savePreferencesParameter(parameter: String,what: String){
        val prefEditor = pref.edit()
        prefEditor.putString(parameter,what)
        prefEditor.apply()
    }
}