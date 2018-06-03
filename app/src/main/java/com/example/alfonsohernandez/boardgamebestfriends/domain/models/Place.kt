package com.example.alfonsohernandez.boardgamebestfriends.domain.models

import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by alfonsohernandez on 06/04/2018.
 */

data class Place (var id: String = "",
                  var photo: String = "",
                  var name: String = "",
                  var address: String = "",
                  var lat: Double = 0.0,
                  var long: Double = 0.0,
                  var firstRuleId: Int = 0,
                  var secondRuleId: Int = 0,
                  var thirdRuleId: Int = 0,
                  var ownerId: String = "",
                  var openPlace: Boolean = true,
                  var firstOpeningHours: String? = "",
                  var secondOpeningHours: String? = "",
                  var thirdOpeningHours: String? = ""){}