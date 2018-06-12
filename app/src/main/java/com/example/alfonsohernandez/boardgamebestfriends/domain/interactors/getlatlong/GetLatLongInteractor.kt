package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getlatlong

import com.google.android.gms.maps.model.LatLng

interface GetLatLongInteractor {
    fun getLatLong(city: String, country: String, strAddress: String): LatLng?
}