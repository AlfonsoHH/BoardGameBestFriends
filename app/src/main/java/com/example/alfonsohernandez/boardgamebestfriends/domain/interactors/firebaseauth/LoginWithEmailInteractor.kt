package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

interface LoginWithEmailInteractor {
    fun loginAuthWithMail(mail: String, password: String): Maybe<AuthResult>
}