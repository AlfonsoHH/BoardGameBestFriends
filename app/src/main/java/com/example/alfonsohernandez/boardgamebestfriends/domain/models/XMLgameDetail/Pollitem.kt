package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false)
data class Pollitem @JvmOverloads constructor(
        @field:Attribute(name = "totalvotes",required = false)
        var totalvotes: String? = null,
        @field:Attribute(name = "namePoll",required = false)
        var name: String? = null,
        @field:Attribute(name = "title",required = false)
        var title: String? = null,
        @field:ElementList(entry = "results", inline = true,required = false)
        var results: List<Results?>? = null
)
