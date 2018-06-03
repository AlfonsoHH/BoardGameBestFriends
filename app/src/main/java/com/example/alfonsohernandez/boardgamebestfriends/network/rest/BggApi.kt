package com.example.alfonsohernandez.boardgamebestfriends.network.rest

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface BggApi {

    @GET("/collection/{user}")
    fun getDiscoveryMoviesRxJava(@Path("user") id: String): Single<ArrayList<Response>>

}