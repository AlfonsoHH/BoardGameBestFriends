package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.Completable

interface ModifyUserInteractor {
    fun modifyFirebaseDataUser(userId: String, user: User): Completable
}