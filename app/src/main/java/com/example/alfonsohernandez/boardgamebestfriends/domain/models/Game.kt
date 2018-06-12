package com.example.alfonsohernandez.boardgamebestfriends.domain.models

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class Game (override val id: String = "",
                 val photo: String = "",
                 val title: String = "",
                 var description: String = "",
                 val minPlayers: Int = 0,
                 val maxPlayers: Int = 0,
                 val playingTime: Int = 0,
                 val rating: Double = 0.0): DatabaseStringItem {}