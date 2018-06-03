package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.components

import android.app.Application
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import dagger.Component

/**
 * Created by alfonsohernandez on 26/03/2018.
 */

@AppScope
@Component(modules = arrayOf(
        ApiModule::class,
        AppModule::class,
        DatabaseModule::class,
        InteractorModule::class,
        PreferenceModule::class,
        RepositoryModule::class
))
interface AppComponent {
    fun inject(app: Application)
    fun plus(presentationModule: PresentationModule): PresentationComponent
}