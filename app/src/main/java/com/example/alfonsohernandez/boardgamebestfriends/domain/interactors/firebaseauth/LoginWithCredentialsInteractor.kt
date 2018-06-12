package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

interface LoginWithCredentialsInteractor {
    fun loginAuthWithCredentials(credential: AuthCredential): Maybe<AuthResult>
}