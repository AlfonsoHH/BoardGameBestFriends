package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

class PlacesRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun getKey(): String{
        return firebaseInstance.child("places").push().key
    }

    fun addPlace(key: String, regionId: String, place: Place){
        place.id = key
        firebaseInstance.child("places").child(regionId).child(key).setValue(place)
    }

    fun modifyPlace(regionId: String, placeId: String, place: Place){
        firebaseInstance.child("places").child(regionId).child(placeId).setValue(place)
    }

    fun removePlace(regionId: String, placeId: String){
        firebaseInstance.child("places").child(regionId).child(placeId).removeValue()
    }

    fun getPlacesRx(city: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("places").child(city)) //.orderByChild("openPlace").equalTo(true)
    }

    fun getUserPlacesRx(regionId: String, userId: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("user-places").child(userId).child(regionId))
    }

    fun getSinglePlaceRx(regionId: String, placeId: String): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("places").child(regionId).child(placeId))
    }

}