package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(strict = false)
data class Averageweight @JvmOverloads constructor(
        @field:Attribute(name = "value", required = false)
        var value: String? = null
)
