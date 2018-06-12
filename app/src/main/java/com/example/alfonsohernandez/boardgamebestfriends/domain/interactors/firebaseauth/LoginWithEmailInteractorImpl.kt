package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

class LoginWithEmailInteractorImpl (private val authRepository: AuthRepository): LoginWithEmailInteractor {
    override fun loginAuthWithMail(mail: String, password: String): Maybe<AuthResult> {
        return authRepository.loginWithMail(mail,password)
    }
}