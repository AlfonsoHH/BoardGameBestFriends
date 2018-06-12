package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.*
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.NamedPaperDatabase
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.StringDatabase
import com.example.alfonsohernandez.boardgamebestfriends.storage.preferences.UserProfileDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = arrayOf(AppModule::class))
class DatabaseModule {

    @Provides
    @AppScope
    fun providesDatabaseUserProfile(context: Context): UserProfileDatabase{
        return UserProfileDatabase(context)
    }

    @Provides
    @AppScope
    @Named("groups")
    fun providesDatabaseGroups(): StringDatabase<Group> {
        return NamedPaperDatabase("groups")
    }

    @Provides
    @AppScope
    @Named("meetings")
    fun providesDatabaseMeetings(): StringDatabase<Meeting> {
        return NamedPaperDatabase("meetings")
    }

    @Provides
    @AppScope
    @Named("places")
    fun providesDatabasePlaces(): StringDatabase<Place> {
        return NamedPaperDatabase("places")
    }

    @Provides
    @AppScope
    @Named("regions")
    fun providesDatabaseRegions(): StringDatabase<Region> {
        return NamedPaperDatabase("regions")
    }

    @Provides
    @AppScope
    @Named("games")
    fun providesDatabaseGames(): StringDatabase<Game> {
        return NamedPaperDatabase("games")
    }
}