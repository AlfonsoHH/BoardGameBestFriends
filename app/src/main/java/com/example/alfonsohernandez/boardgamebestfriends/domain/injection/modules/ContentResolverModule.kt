package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.contentresolver.GetPathFromUri
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(AppModule::class))
class ContentResolverModule {

    @Provides
    @AppScope
    fun providesContentResolver(context: Context): GetPathFromUri {
        return GetPathFromUri(context)
    }
}