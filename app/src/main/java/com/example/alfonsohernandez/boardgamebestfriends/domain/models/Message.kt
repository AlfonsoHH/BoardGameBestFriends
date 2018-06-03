package com.example.alfonsohernandez.boardgamebestfriends.domain.models

import java.util.*

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class Message (val userId: String = "",
                    val text: String = "",
                    val date: String = "") {}