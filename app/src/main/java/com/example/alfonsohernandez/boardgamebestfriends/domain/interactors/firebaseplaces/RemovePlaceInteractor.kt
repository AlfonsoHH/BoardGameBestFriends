package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces

import io.reactivex.Completable

interface RemovePlaceInteractor {
    fun removeFirebaseDatePlace(regionId: String, placeId: String): Completable
}