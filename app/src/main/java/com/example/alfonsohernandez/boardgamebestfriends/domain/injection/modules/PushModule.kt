package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.*
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMpush
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMtopic
import dagger.Module
import dagger.Provides

@Module
class PushModule {
    @Provides
    @AppScope
    fun providesFCMtopic(): FCMtopic {
        return FCMtopic()
    }

    @Provides
    @AppScope
    fun providesFCMpush(): FCMpush {
        return FCMpush()
    }

    @Provides
    @AppScope
    fun providesFCMHandler(): FCMHandler {
        return FCMHandler()
    }

}