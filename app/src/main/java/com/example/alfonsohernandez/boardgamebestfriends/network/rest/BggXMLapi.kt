package com.example.alfonsohernandez.boardgamebestfriends.network.rest

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BggXMLapi {

    @GET("/xmlapi2/thing")
    fun getGameDetailRxJava(@Query("id") id: String,@Query("stats") stats: String): Single<com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail.Items>

    @GET("/xmlapi2/collection")
    fun getGameColletionRxJava(@Query("username") username: String,@Query("own") own: String,@Query("excludesubtype") excludesubtype: String): Single<com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameCollection.Items>

}