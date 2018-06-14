package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.components

import android.app.Application
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMpush
import dagger.Component

/**
 * Created by alfonsohernandez on 26/03/2018.
 */

@AppScope
@Component(modules = arrayOf(
        ApiModule::class,
        AppModule::class,
        ContentResolverModule::class,
        DatabaseModule::class,
        GeocoderModule::class,
        InteractorModule::class,
        PreferenceModule::class,
        RepositoryModule::class,
        PushModule::class
))
interface AppComponent {
    fun inject(app: Application)
    fun plus(presentationModule: PresentationModule): PresentationComponent
    fun inject(target: FCMpush)
}