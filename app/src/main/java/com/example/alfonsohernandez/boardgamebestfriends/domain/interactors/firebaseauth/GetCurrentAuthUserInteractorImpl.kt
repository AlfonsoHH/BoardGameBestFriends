package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable

class GetCurrentAuthUserInteractorImpl(private val authRepository: AuthRepository): GetCurrentAuthUserInteractor {
    override fun getFirebaseCurrentAuthUser(): FirebaseUser? {
        return authRepository.getUser()
    }
}