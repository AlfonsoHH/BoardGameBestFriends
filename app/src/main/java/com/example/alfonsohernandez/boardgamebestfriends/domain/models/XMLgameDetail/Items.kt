package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.Attribute

@Root(strict = false)
data class Items @JvmOverloads constructor(
        @field:Element(name = "item",required = false)
        var item: Item? = null,
        @field:Attribute(name = "termsofuse",required = false)
        var termsofuse: String? = null
)
