package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

interface CreateMailUserInteractor {
    fun createAuthMailUser(email: String, password: String): Maybe<AuthResult>
}