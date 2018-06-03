package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.Completable

interface AddUserInteractor {
    fun addFirebaseDataUser(userId: String,user: User): Completable
}