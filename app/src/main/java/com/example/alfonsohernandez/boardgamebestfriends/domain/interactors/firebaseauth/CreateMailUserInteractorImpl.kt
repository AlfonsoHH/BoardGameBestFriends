package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

class CreateMailUserInteractorImpl (private val authRepository: AuthRepository): CreateMailUserInteractor {
    override fun createAuthMailUser(email: String, password: String): Maybe<AuthResult> {
        return authRepository.createMailUser(email,password)
    }
}