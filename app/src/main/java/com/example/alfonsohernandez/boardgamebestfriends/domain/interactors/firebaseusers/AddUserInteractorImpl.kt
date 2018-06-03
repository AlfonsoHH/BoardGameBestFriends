package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.UsersRepository
import io.reactivex.Completable

class AddUserInteractorImpl (private val usersRepository: UsersRepository): AddUserInteractor{
    override fun addFirebaseDataUser(userId: String,user: User): Completable {
        return Completable.create {
            try {
                usersRepository.addUser(userId,user)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}