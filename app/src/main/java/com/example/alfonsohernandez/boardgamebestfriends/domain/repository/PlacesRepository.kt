package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

class PlacesRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun addPlace(regionId: String, place: Place){
        val key = firebaseInstance.child("places").push().key
        place.id = key
        firebaseInstance.child("places").child(regionId).child(key).setValue(place)
    }

    fun addPlaceToUser(regionId: String, userId: String, placeId: String){
        firebaseInstance.child("user-places").child(userId).child(regionId).child(placeId).setValue(true)
    }

    fun modifyPlace(regionId: String, placeId: String, place: Place){
        firebaseInstance.child("places").child(regionId).child(placeId).setValue(place)
    }

    fun removePlace(regionId: String, placeId: String){
        firebaseInstance.child("places").child(regionId).child(placeId).removeValue()
    }

    fun getOpenPlacesRx(city: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("places").child(city))     //.orderByChild("open").startAt(true)
    }

    fun getUserPlacesRx(regionId: String, userId: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("user-places").child(userId).child(regionId))
    }

    fun getSinglePlaceRx(regionId: String, placeId: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("places").child(regionId).child(placeId))
    }

}