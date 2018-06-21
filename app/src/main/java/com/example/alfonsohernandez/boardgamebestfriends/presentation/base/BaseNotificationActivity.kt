package com.example.alfonsohernandez.boardgamebestfriends.presentation.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.Snacktory
import com.google.firebase.messaging.RemoteMessage

abstract class BaseNotificationActivity: AppCompatActivity() {

    var topic = ""
    var title = ""
    var text = ""

    fun setNotificacion(rm: RemoteMessage){
        topic = rm.from!!
        topic = topic.substring(8, topic.length)
        title = rm.notification!!.title!!
        text = rm.notification!!.title + "\n" + rm.notification!!.body
    }

    fun allNotifications(){
        chat()
        goToGroups()
        goToGroupDetail()
        goToMeetings()
        goToMeetingDetail()
        addedToNewGroup()
        goToMeetingSoon()
    }

    fun chat() {
        if (title.contains("Chat")) {
            Snacktory.snacktoryBase(this, text, Runnable {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("id", topic)
                startActivity(intent)
            })
        }
    }

    fun goToGroupDetail(){
        if(title.contains("Group user:")) {
            Snacktory.snacktoryBase(this, text, Runnable {
                val intent = Intent(this, GroupDetailActivity::class.java)
                intent.putExtra("id", topic)
                startActivity(intent)
            })
        }
    }

    fun addedToNewGroup(){
        if(title.contains("Added to:")){
            Snacktory.snacktoryNoAction(this,text)
        }
    }

    fun goToGroups(){
        if(title.contains("Group removed")) {
            Snacktory.snacktoryBase(this, text, Runnable {
                val intent = Intent(this, TabActivity::class.java)
                intent.putExtra("otherTab", 1)
                startActivity(intent)
            })
        }
    }

    fun goToMeetings(){
        if(title.contains("Meeting removed")) {
            Snacktory.snacktoryBase(this, text, Runnable {
                val intent = Intent(this, TabActivity::class.java)
                startActivity(intent)
            })
        }
    }

    fun goToMeetingDetail(){
        if(title.contains("Meeting modified") || title.contains("Meeting starting soon")) {
            Snacktory.snacktoryBase(this, text, Runnable {
                val intent = Intent(this, MeetingDetailActivity::class.java)
                intent.putExtra("id", topic)
                startActivity(intent)
            })
        }
    }

    fun goToMeetingSoon(){
        if(title.contains("Meeting starting soon:") || title.contains("Meeting starting soon")) {
            Snacktory.snacktoryBase(this, text, Runnable {
                val intent = Intent(this, MeetingDetailActivity::class.java)
                intent.putExtra("id", topic)
                startActivity(intent)
            })
        }
    }

}