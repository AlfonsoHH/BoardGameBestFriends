package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(strict = false)
data class Linkitem @JvmOverloads constructor(
        @field:Attribute(name = "type",required = false)
        var type: String? = null,
        @field:Attribute(name = "value",required = false)
        var value: String? = null,
        @field:Attribute(name = "id",required = false)
        var id: String? = null
)
