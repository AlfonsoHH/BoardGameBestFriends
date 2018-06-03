package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.storage.preferences.UserProfileDatabase
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(AppModule::class))
class DatabaseModule {

    @Provides
    @AppScope
    fun providesDatabaseUserProfile(context: Context): UserProfileDatabase{
        return UserProfileDatabase(context)
    }
}