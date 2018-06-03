package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe

class GamesRepository {
    val firebaseInstance = FirebaseDatabase.getInstance().getReference()

    fun addGame(game: Game){
        firebaseInstance.child("games").child(game.id).setValue(game)
    }

    fun addGameToUser(userId: String, gameId: String){
        firebaseInstance.child("user-games").child(userId).child(gameId).setValue(true)
    }

    fun removeGameToUser(userId: String, gameId: String){
        firebaseInstance.child("user-games").child(userId).child(gameId).removeValue()
    }

    fun addGameToGroup(groupId: String, gameId: String){
        firebaseInstance.child("group-games").child(groupId).child(gameId).setValue(true)
    }

    fun removeGameToGroup(groupId: String, gameId: String){
        firebaseInstance.child("groups").child(groupId).child(gameId).removeValue()
    }

    fun addGameToPlace(placeId: String, gameId: String){
        firebaseInstance.child("place-games").child(placeId).child(gameId).setValue(true)
    }

    fun removeGameToPlace(placeId: String, gameId: String){
        firebaseInstance.child("places").child(placeId).child(gameId).removeValue()
    }

    fun getAllGamesRx(): Maybe<DataSnapshot>{
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("games").orderByChild("title"))
    }

    fun getUserGamesRx(userId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("user-games").child(userId))
    }

    fun getGroupGamesRx(groupId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("group-games").child(groupId))
    }

    fun getPlaceGamesRx(placeId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("place-games").child(placeId))
    }

    fun getSingleGameRx(gameId: String): Maybe<DataSnapshot> {
        return RxFirebaseDatabase.observeSingleValueEvent(firebaseInstance.child("games").child(gameId))
    }
}