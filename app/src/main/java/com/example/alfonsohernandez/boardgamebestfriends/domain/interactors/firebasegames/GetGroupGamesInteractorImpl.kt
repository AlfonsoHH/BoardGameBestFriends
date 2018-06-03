package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.GamesRepository
import com.google.firebase.database.DataSnapshot
import io.reactivex.Maybe

class GetGroupGamesInteractorImpl (private val gamesRepository: GamesRepository): GetGroupGamesInteractor{
    override fun getFirebaseDataGroupGames(groupId: String): Maybe<DataSnapshot> {
        return gamesRepository.getGroupGamesRx(groupId)
    }
}