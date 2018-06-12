package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.google.firebase.database.*
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

/**
 * Created by alfonsohernandez on 26/03/2018.
 */

class MeetingsRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun addMeeting(regionId: String, meeting: Meeting){
        val key = firebaseInstance.child("meetings").push().key
        meeting.id = key
        firebaseInstance.child("meetings").child(regionId).child(key).setValue(meeting)
    }

    fun addMeetingToUser(regionId: String, userId: String, playing: Boolean, meetingId: String){
        firebaseInstance.child("user-meetings").child(userId).child(regionId).child(meetingId).setValue(playing)
//        if(playing) {
//            firebaseInstance.child("meeting-users").child(regionId).child(meetingId).child(userId).setValue(true)
//        }else{
//            firebaseInstance.child("meeting-users").child(regionId).child(meetingId).child(userId).removeValue()
//        }
    }

    fun addMeetingToPlace(placeId: String, meetingId: String){
        firebaseInstance.child("place-meetings").child(placeId).child(meetingId).setValue(true)
    }

    fun modifyMeeting(regionId: String, meetingId: String, meeting: Meeting){
        firebaseInstance.child("meetings").child(regionId).child(meetingId).setValue(meeting)
    }

    fun removeMeeting(regionId: String, meetingId: String){
        firebaseInstance.child("meetings").child(regionId).child(meetingId).removeValue()
    }

    fun removeMeetingToUser(regionId: String, userId: String, meetingId: String){
        firebaseInstance.child("user-meetings").child(userId).child(regionId).child(meetingId).removeValue()
        firebaseInstance.child("meeting-users").child(regionId).child(meetingId).child(userId).removeValue()
    }

    fun getOpenMeetingsRx(regionId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("meetings").child(regionId))
    }

    fun getUserMeetingsRx(regionId: String, userId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("user-meetings").child(userId).child(regionId))
    }

    fun getGroupMeetingsRx(groupId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("group-meetings").child(groupId))
    }

    fun getPlaceMeetingsRx(placeId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("place-meetings").child(placeId))
    }

    fun getSingleMeetingRx(regionId: String, meetingId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("meetings").child(regionId).child(meetingId))
    }
}