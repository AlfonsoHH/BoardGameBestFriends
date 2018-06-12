package com.example.alfonsohernandez.boardgamebestfriends.domain.geocoder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class Geocoder (context: Context) {

    var coder: Geocoder

    init {
        coder = Geocoder(context)
    }

    fun getLatLongFromAddress(city: String, country: String, strAddress: String): LatLng? {

        val address: List<Address>
        var p1 = LatLng(0.0,0.0)

        val modifiedAddress = strAddress + " " + city + " " + country

        try {
            address = coder.getFromLocationName(modifiedAddress, 5)
            if (address == null) {
                return null
            }
            if(address.get(0) != null) {
                val location = address.get(0)
                p1 = LatLng(location.getLatitude(), location.getLongitude())
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return p1
    }
}