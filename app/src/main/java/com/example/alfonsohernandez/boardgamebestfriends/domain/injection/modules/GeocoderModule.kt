package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.geocoder.Geocoder
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(AppModule::class))
class GeocoderModule {

    @Provides
    @AppScope
    fun providesGeocoder(context: Context): Geocoder {
        return Geocoder(context)
    }
}