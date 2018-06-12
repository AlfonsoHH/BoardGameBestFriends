package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import io.reactivex.Completable

interface AddGroupInteractor {
    fun addFirebaseDataGroup(key: String, group: Group, userList: ArrayList<String>): Completable
    fun getKey():String
}