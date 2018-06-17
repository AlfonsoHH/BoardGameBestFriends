package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameCollection

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(strict = false)
data class Items @JvmOverloads constructor(
        @field:ElementList(entry = "item", inline = true,required = false)
        var item: List<Item?>? = null,
        @field:Attribute(name = "totalitems",required = false)
        var totalitems: String? = null,
        @field:Attribute(name = "termsofuse",required = false)
        var termsofuse: String? = null,
        @field:Attribute(name = "pubdate",required = false)
        var pubdate: String? = null
)
