package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail.Items
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggXMLapi
import io.reactivex.Single

class GetBggGameDetailInteractorImpl(private val bggXmlApi: BggXMLapi) : GetBggGameDetailInteractor {
    override fun getGameDetail(gameId: String): Single<Items> {
        return bggXmlApi.getGameDetailRxJava(gameId,"1")
    }
}