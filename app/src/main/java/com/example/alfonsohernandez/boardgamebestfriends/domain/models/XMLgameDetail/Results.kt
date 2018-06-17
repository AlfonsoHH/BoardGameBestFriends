package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false)
data class Results @JvmOverloads constructor(
        @field:ElementList(entry = "result", inline = true,required = false)
        var result: List<Result?>? = null,
        @field:Attribute(name = "numplayers",required = false)
        var numplayers: String? = null
)
