package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.facebook.login.LoginManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe

class AuthRepository {

    val firebaseInstance = FirebaseAuth.getInstance()

    fun loginWithMail(mail: String, password: String): Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(firebaseInstance,mail,password)
    }

    fun createMailUser(mail: String, password: String): Maybe<AuthResult>{
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseInstance,mail,password)
    }

    fun loginWithCredentials(credential: AuthCredential): Maybe<AuthResult>{
        return RxFirebaseAuth.signInWithCredential(firebaseInstance,credential)
    }

}