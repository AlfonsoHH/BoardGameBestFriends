package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggApi
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggXMLapi
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.ExpiredSessionInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.IOException

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
                .addInterceptor(ExpiredSessionInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    inner class ExpiredSessionInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code() == 202) {
                val newRequest = request.newBuilder().build()
                Thread.sleep(2000)
                return chain.proceed(newRequest)
            } else {
                return response
            }
        }
    }

    @Provides
    @AppScope
    fun createService(okHttpClient: OkHttpClient): BggApi {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://bgg-json.azurewebsites.net/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val service = retrofit.create<BggApi>(BggApi::class.java)


        return service
    }

    @Provides
    @AppScope
    fun createServiceXMLapi(okHttpClient: OkHttpClient): BggXMLapi {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.boardgamegeek.com/")
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        val service = retrofit.create<BggXMLapi>(BggXMLapi::class.java)

        return service
    }
}