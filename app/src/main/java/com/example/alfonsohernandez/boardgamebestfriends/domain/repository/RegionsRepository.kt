package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

class RegionsRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun getAllRegionsRx(): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("regions"))
    }

    fun getRegionRx(regionId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("regions").child(regionId))
    }
}