package com.example.alfonsohernandez.boardgamebestfriends.domain.models

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class User (var id: String = "",
                 var email: String = "",
                 var userName: String = "",
                 var photo: String = "",
                 var service: String = "",
                 var regionId: String = "") {}