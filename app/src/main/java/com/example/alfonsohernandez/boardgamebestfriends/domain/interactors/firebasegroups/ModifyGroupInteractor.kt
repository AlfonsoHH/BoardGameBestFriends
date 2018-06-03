package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import io.reactivex.Completable

interface ModifyGroupInteractor {
    fun modifyFirebaseDataGroup(groupId: String, group: Group): Completable
}