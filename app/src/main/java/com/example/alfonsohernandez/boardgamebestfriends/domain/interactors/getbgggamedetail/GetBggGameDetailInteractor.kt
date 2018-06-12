package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamedetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail.Items
import io.reactivex.Single

interface GetBggGameDetailInteractor {
    fun getGameDetail(gameId: String): Single<Items>
}