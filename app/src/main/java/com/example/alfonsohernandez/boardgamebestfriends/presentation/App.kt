package com.example.alfonsohernandez.boardgamebestfriends.presentation

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.components.AppComponent
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.components.DaggerAppComponent
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.AppModule
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class App : Application(), Application.ActivityLifecycleCallbacks {

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

    var foregroundActivity: Activity? = null


    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        component.inject(this)

        instance = this

    }

    override fun onActivityResumed(activity: Activity) {
        foregroundActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        foregroundActivity = null
    }

    override fun onActivityStarted(activity: Activity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityDestroyed(activity: Activity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityStopped(activity: Activity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}