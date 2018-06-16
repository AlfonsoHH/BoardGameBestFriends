package com.example.alfonsohernandez.boardgamebestfriends.presentation.utils

import android.app.Activity
import android.content.Context
import android.view.View
import de.mateware.snacky.Snacky

object Snacktory {

    fun snacktoryBase(activity: Activity, text: String, listener: Runnable){
        Snacky
                .builder()
                .setActivity(activity)
                .setActionText("Action")
                .setActionClickListener(object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        listener.run()
                    }
                })
                .setText(text)
                .build()
                .show()
    }

    fun snacktoryNoAction(activity: Activity, text: String){
        Snacky
                .builder()
                .setActivity(activity)
                .setText(text)
                .build()
                .show()
    }

}