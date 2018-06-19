package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameCollection.Items
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggXMLapi
import io.reactivex.Single

class GetBggXmlGameCollectionInteractorImpl (private val bggXmlApi: BggXMLapi) : GetBggXmlGameCollectionInteractor {
    override fun getGameCollectionXML(bggId: String): Single<Items> {
        return bggXmlApi.getGameColletionRxJava(bggId,"1","boardgameexpansion")
    }
}