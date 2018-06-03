package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.UsersRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetSingleUserFromMailInteractorImpl(private val usersRepository: UsersRepository): GetSingleUserFromMailInteractor  {
    override fun getFirebaseDataSingleUserFromMail(email: String): Maybe<DataSnapshot> {
        return usersRepository.getSingleUserFromEmail(email)
    }
}