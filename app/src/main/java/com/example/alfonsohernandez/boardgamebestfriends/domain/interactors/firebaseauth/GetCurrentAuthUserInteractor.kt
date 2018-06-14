package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable

interface GetCurrentAuthUserInteractor {
    fun getFirebaseCurrentAuthUser(): FirebaseUser?
}