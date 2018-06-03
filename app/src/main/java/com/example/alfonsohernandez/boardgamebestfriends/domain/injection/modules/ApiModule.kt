package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = arrayOf(AppModule::class))
class ApiModule() {

    @Provides
    @AppScope
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY   //BODY
        return loggingInterceptor
    }

    @Provides
    @AppScope
    fun providesHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @AppScope
    fun createService(okHttpClient: OkHttpClient): BggApi {
        val retrofit = Retrofit.Builder()
                .baseUrl("\thttps://bgg-json.azurewebsites.net/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val service = retrofit.create<BggApi>(BggApi::class.java)


        return service
    }
}