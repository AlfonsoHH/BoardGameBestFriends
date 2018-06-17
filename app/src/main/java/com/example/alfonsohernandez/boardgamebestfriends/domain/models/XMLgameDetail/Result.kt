package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(strict = false)
data class Result @JvmOverloads constructor(
        @field:Attribute(name = "numvotes",required = false)
        var numvotes: String? = null,
        @field:Attribute(name = "level",required = false)
        var level: String? = null,
        @field:Attribute(name = "value",required = false)
        var value: String? = null
)
