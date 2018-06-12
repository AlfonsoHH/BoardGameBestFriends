package com.example.alfonsohernandez.boardgamebestfriends.domain.models

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class Region (override val id: String = "",
                   val city: String = "",
                   val country: String = "",
                   val lat: Double = 0.0,
                   val long: Double = 0.0): DatabaseStringItem {}