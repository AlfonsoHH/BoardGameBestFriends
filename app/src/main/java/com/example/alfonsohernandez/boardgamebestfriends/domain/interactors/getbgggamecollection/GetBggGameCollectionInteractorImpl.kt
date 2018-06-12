package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Response
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggApi
import io.reactivex.Single

class GetBggGameCollectionInteractorImpl(private val bggApi: BggApi) : GetBggGameCollectionInteractor {
    override fun getGameCollection(gameId: String): Single<ArrayList<Response>> {
        return bggApi.getDiscoveryMoviesRxJava(gameId)
    }
}