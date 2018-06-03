package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Response
import io.reactivex.Single

interface GetBggGameCollectionInteractor {
    fun getGameCollection(user: String): Single<ArrayList<Response>>
}