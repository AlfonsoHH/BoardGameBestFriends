package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.Completable

/**
 * Created by alfonsohernandez on 27/03/2018.
 */

interface SaveUserProfileInteractor {
    fun save(userProfile: User?): Completable
}