package com.example.alfonsohernandez.boardgamebestfriends.presentation.utils

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.google.firebase.messaging.RemoteMessage

class NotificationFilter(activity: Activity, rm: RemoteMessage) {

    var topic = ""
    var title = ""
    var text = ""
    var activity: Activity

    init {
        topic = rm.from!!
        topic = topic.substring(8, topic.length)
        title = rm.notification!!.title!!
        text = rm.notification!!.title + "\n" + rm.notification!!.body
        this.activity = activity
    }

    fun chat() {
        if (title.contains("Chat")) {
            Snacktory.snacktoryBase(activity, text, Runnable {
                var intent = Intent(activity, ChatActivity::class.java)
                var extras = intent.extras
                extras.putString("id", topic)
                startActivity(activity,intent,extras)
            })
        }
    }

    fun groupUser(){
        if(title.contains("Group user")) {
            Snacktory.snacktoryBase(activity, text, Runnable {
                var intent = Intent(activity, GroupDetailActivity::class.java)
                var extras = intent.extras
                extras.putString("id", topic)
                startActivity(activity,intent,extras)
            })
        }
    }

    fun groupRemoved(){
        if(title.contains("Group removed")) {
            Snacktory.snacktoryBase(activity, text, Runnable {
                var intent = Intent(activity, TabActivity::class.java)
                var extras = intent.extras
                extras.putInt("otherTab", 1)
                startActivity(activity,intent,extras)
            })
        }
    }

    fun meetingRemoved(){
        if(title.contains("Meeting removed")) {
            Snacktory.snacktoryBase(activity, text, Runnable {
                var intent = Intent(activity, TabActivity::class.java)
                var extras = intent.extras
                startActivity(activity,intent,extras)
            })
        }
    }

    fun meetingModified(){
        if(title.contains("Meeting modified")) {
            Snacktory.snacktoryBase(activity, text, Runnable {
                var intent = Intent(activity, MeetingDetailActivity::class.java)
                var extras = intent.extras
                extras.putString("id", topic)
                startActivity(activity,intent,extras)
            })
        }
    }
}