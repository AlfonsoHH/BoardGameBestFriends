package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameCollection.Items
import io.reactivex.Single

interface GetBggXmlGameCollectionInteractor {
    fun getGameCollectionXML(bggId: String): Single<Items>
}