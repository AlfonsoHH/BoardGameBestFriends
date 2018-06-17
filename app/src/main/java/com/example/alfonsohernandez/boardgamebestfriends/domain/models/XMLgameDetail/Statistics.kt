package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Statistics @JvmOverloads constructor(
        @field:Attribute(name = "page", required = false)
        var page: String? = null,
        @field:Element(name = "ratings", required = false)
        var ratings: Ratings? = null
)
