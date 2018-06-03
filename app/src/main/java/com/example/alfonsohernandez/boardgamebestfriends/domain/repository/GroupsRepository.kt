package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Flowable
import io.reactivex.Maybe

class GroupsRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun addGroup(group: Group, users: ArrayList<String>){
        val key = firebaseInstance.child("groups").push().key
        group.id = key
        firebaseInstance.child("groups").child(key).setValue(group)
        for (user in users) {
            firebaseInstance.child("group-users").child(key).child(user).setValue(true)
            firebaseInstance.child("user-groups").child(user).child(key).setValue(true)
        }
    }

    fun addGroupToUser(userId: String, groupId: String){
        //firebaseInstance.child("users").child(userId).child("groups").setValue(group)
        firebaseInstance.child("user-groups").child(userId).child(groupId).setValue(true)
        firebaseInstance.child("group-users").child(groupId).child(userId).setValue(true)
    }

    fun modifyGroup(groupId: String, group: Group){
        firebaseInstance.child("groups").child(groupId).setValue(group)
    }

//    fun modifyGroupToUser(userId: String, groupId: String, group: Group){
//        firebaseInstance.child("users").child(userId).child("groups").child(groupId).setValue(group)
//    }

    fun removeGroup(groupId: String){
        firebaseInstance.child("groups").child(groupId).removeValue()
    }

    fun removeGroupToUser(userId: String, groupId: String){
        firebaseInstance.child("user-groups").child(userId).child(groupId).removeValue()
        firebaseInstance.child("group-users").child(groupId).child(userId).removeValue()
    }

//    fun removeGroupToUser(userId: String, groupId: String){
//        //firebaseInstance.child("users").child(userId).child("groups").child(groupId).removeValue()
//        firebaseInstance.child("user-groups").child(userId).child(groupId).removeValue()
//        firebaseInstance.child("group-users").child(groupId).child(userId).removeValue()
//    }

    fun getUserGroupsRx(userId: String): Maybe<DataSnapshot> {
        //return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("users").child(userId).child("groups"))
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("user-groups").child(userId))
    }

    fun getSingleGroupRx(groupId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("groups").child(groupId))
    }

}