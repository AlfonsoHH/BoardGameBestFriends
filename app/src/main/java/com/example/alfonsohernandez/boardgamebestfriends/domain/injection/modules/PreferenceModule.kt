package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

/**
 * Created by alfonsohernandez on 27/03/2018.
 */
import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.PreferencesManager
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(AppModule::class))
class PreferenceModule {
    @Provides
    @AppScope
    fun providesPreferences(context: Context): PreferencesManager {
        return PreferencesManager(context)
    }
}