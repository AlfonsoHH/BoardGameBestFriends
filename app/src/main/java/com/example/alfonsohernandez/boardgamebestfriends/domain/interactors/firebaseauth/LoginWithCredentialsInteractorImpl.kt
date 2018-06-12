package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

class LoginWithCredentialsInteractorImpl (private val authRepository: AuthRepository): LoginWithCredentialsInteractor {
    override fun loginAuthWithCredentials(credential: AuthCredential): Maybe<AuthResult> {
        return authRepository.loginWithCredentials(credential)
    }
}