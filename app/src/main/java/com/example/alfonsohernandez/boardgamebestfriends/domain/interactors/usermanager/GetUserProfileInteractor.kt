package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User


/**
 * Created by alfonsohernandez on 27/03/2018.
 */
interface GetUserProfileInteractor {
    fun getProfile(): User?
}