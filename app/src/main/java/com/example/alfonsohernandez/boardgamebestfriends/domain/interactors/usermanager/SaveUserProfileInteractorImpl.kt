package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.storage.preferences.UserProfileDatabase
import io.reactivex.Completable

/**
 * Created by alfonsohernandez on 27/03/2018.
 */

class SaveUserProfileInteractorImpl(private val database: UserProfileDatabase) : SaveUserProfileInteractor {
    override fun save(userProfile: User?): Completable {
        return Completable.create {
            try {
                database.set(userProfile)
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}