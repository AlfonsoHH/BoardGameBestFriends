package com.example.alfonsohernandez.boardgamebestfriends.domain.models

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class Meeting (override var id: String = "",
                    val title: String = "",
                    val description: String = "",
                    val date: String = "",
                    var groupId: String = "",
                    var placeId: String = "",
                    var placePhoto: String = "",
                    var gameId: String = "",
                    var gamePhoto: String = "",
                    val creatorId: String = "",
                    var vacants: Int = 0,
                    var label: String? = null) : DatabaseStringItem {}