package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.UsersRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetAllUsersInteractorImpl (private val usersRepository: UsersRepository): GetAllUsersInteractor {
    override fun getFirebaseDataAllUsers(): Maybe<DataSnapshot> {
        return usersRepository.getAllUsersRx()
    }
}