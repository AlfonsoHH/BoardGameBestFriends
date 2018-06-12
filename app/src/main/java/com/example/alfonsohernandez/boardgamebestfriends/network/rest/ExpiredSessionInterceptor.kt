package com.example.alfonsohernandez.boardgamebestfriends.network.rest

import okhttp3.Interceptor
import okhttp3.Response

class ExpiredSessionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code() == 202) {
            val newRequest = request.newBuilder().build()
            return chain.proceed(newRequest)
        } else {
            return response
        }
    }

}