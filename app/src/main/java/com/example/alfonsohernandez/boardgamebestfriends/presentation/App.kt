package com.example.alfonsohernandez.boardgamebestfriends.presentation

import android.app.Application
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.components.AppComponent
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.components.DaggerAppComponent
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.AppModule
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class App : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        lateinit var instance: App
    }

    @Inject
    lateinit var app : Application

    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        component.inject(this)

        instance = this

    }

}