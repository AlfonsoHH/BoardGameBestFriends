package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

class UsersRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun addUser(userId: String, user: User){
        firebaseInstance.child("users").child(userId).setValue(user)
    }

    fun modifyUser(userId: String, user: User){
        firebaseInstance.child("users").child(userId).setValue(user)
    }

    fun getAllUsersRx(): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("users"))
    }

    fun getGroupUsersRX(groupId: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("group-users").child(groupId))
    }

    fun getMeetingUsersRX(regionId: String, meetingId: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("meeting-users").child(regionId).child(meetingId))
    }

    fun getSingleUserRx(userId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("users").child(userId))
    }

    fun getSingleUserFromEmail(email: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("users").orderByChild("email").equalTo(email))
    }

}