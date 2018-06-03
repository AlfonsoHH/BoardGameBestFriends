package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import io.reactivex.Completable

interface AddGameToGroupInteractor {
    fun AddFirebaseDataGameToGroup(groupId: String, gameId: String): Completable
}