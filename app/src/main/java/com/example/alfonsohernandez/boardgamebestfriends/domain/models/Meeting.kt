package com.example.alfonsohernandez.boardgamebestfriends.domain.models

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class Meeting (var id: String = "",
                    val title: String = "",
                    val description: String = "",
                    val date: String = "",
                    val groupId: String = "",
                    val placeId: String = "",
                    val placePhoto: String = "",
                    val gameId: String = "",
                    val gamePhoto: String = "",
                    val creatorId: String = "",
                    var vacants: Int = 0,
                    var label: String? = null) {}