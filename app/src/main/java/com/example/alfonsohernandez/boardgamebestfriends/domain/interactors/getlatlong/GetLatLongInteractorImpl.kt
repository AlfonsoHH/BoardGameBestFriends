package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getlatlong

import com.example.alfonsohernandez.boardgamebestfriends.domain.geocoder.Geocoder
import com.google.android.gms.maps.model.LatLng

class GetLatLongInteractorImpl(private val geocoder: Geocoder): GetLatLongInteractor {
    override fun getLatLong(city: String, country: String, strAddress: String): LatLng? {
        return geocoder.getLatLongFromAddress(city,country,strAddress)
    }
}