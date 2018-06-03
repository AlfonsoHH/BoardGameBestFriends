package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.UsersRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetSingleUserInteractorImpl (private val usersRepository: UsersRepository): GetSingleUserInteractor {
    override fun getFirebaseDataSingleUser(userId: String): Maybe<DataSnapshot> {
        return usersRepository.getSingleUserRx(userId)
    }
}