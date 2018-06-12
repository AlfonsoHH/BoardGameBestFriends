package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.*
import com.example.alfonsohernandez.boardgamebestfriends.storage.database.StringDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

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

    @Provides
    @AppScope
    fun providesImagesRepository(): ImageRepository {
        return ImageRepository()
    }

    @Provides
    @AppScope
    fun providesAuthRepository(): AuthRepository {
        return AuthRepository()
    }

    @Provides
    @AppScope
    fun providesPaperGroupRepository(@Named("groups") database: StringDatabase<Group>): PaperGroupsRepository {
        return PaperGroupsRepository(database)
    }

    @Provides
    @AppScope
    fun providesPaperMeetingRepository(@Named("meetings") database: StringDatabase<Meeting>): PaperMeetingsRepository {
        return PaperMeetingsRepository(database)
    }

    @Provides
    @AppScope
    fun providesPaperPlaceRepository(@Named("places") database: StringDatabase<Place>): PaperPlacesRepository {
        return PaperPlacesRepository(database)
    }

    @Provides
    @AppScope
    fun providesPaperRegionRepository(@Named("regions") database: StringDatabase<Region>): PaperRegionsRepository {
        return PaperRegionsRepository(database)
    }

    @Provides
    @AppScope
    fun providesPaperGameRepository(@Named("games") database: StringDatabase<Game>): PaperGamesRepository {
        return PaperGamesRepository(database)
    }
}