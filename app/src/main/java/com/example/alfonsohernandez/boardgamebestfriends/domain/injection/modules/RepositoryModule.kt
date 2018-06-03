package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.*
import dagger.Module
import dagger.Provides

/**
 * Created by alfonsohernandez on 27/03/2018.
 */

@Module
class RepositoryModule {
    @Provides
    @AppScope
    fun providesChatRepository(): ChatRepository {
        return ChatRepository()
    }

    @Provides
    @AppScope
    fun providesGamesRepository(): GamesRepository {
        return GamesRepository()
    }

    @Provides
    @AppScope
    fun providesGroupsRepository(): GroupsRepository {
        return GroupsRepository()
    }

    @Provides
    @AppScope
    fun providesMeetingsRepository(): MeetingsRepository {
        return MeetingsRepository()
    }

    @Provides
    @AppScope
    fun providesPlacesRepository(): PlacesRepository {
        return PlacesRepository()
    }

    @Provides
    @AppScope
    fun providesRegionRepository(): RegionsRepository {
        return RegionsRepository()
    }

    @Provides
    @AppScope
    fun providesUsersRepository(): UsersRepository {
        return UsersRepository()
    }
}