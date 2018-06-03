package com.example.alfonsohernandez.boardgamebestfriends.domain.models

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class Group (var id: String = "",
                  var photo: String = "",
                  val title: String = "",
                  var subtitle: String = "",
                  val creator: String = "") {}