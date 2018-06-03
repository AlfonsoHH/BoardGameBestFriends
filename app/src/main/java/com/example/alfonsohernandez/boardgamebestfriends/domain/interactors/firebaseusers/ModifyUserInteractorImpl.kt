package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.UsersRepository
import io.reactivex.Completable

class ModifyUserInteractorImpl (private val usersRepository: UsersRepository): ModifyUserInteractor {
    override fun modifyFirebaseDataUser(userId: String, user: User): Completable {
        return Completable.create {
            try {
                usersRepository.modifyUser(userId,user)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}