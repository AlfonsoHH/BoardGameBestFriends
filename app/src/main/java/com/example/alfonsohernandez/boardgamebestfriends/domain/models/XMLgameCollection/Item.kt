package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameCollection

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Item @JvmOverloads constructor(
        @field:Element(name = "image", required = false)
        var image: String? = null,
        @field:Element(name = "thumbnail", required = false)
        var thumbnail: String? = null,
        @field:Attribute(name = "objectid", required = false)
        var objectid: String? = null,
        @field:Attribute(name = "subtype", required = false)
        var subtype: String? = null,
        @field:Element(name = "yearpublished", required = false)
        var yearpublished: String? = null,
        @field:Element(name = "name", required = false)
        var name: Name? = null,
        @field:Attribute(name = "collid", required = false)
        var collid: String? = null,
        @field:Attribute(name = "objecttype", required = false)
        var objecttype: String? = null,
        @field:Element(name = "numplays", required = false)
        var numplays: String? = null,
        @field:Element(name = "status", required = false)
        var status: Status? = null
)
