package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.storage.preferences.UserProfileDatabase

/**
 * Created by alfonsohernandez on 27/03/2018.
 */

class GetUserProfileInteractorImpl(private val preferences: UserProfileDatabase) : GetUserProfileInteractor {

    override fun getProfile(): User? {
        return preferences.get()
    }

}