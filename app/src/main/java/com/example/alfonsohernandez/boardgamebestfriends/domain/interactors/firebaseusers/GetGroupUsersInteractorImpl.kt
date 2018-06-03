package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.UsersRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetGroupUsersInteractorImpl (private val usersRepository: UsersRepository): GetGroupUsersInteractor{
    override fun getFirebaseDataGroupUsers(groupId: String): Maybe<DataSnapshot> {
        return usersRepository.getGroupUsersRX(groupId)
    }
}