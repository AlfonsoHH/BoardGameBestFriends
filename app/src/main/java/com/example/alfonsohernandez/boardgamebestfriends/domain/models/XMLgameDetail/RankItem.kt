package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(strict = false)
data class RankItem @JvmOverloads constructor(
        @field:Attribute(name = "friendlyname", required = false)
        var friendlyname: String? = null,
        @field:Attribute(name = "name", required = false)
        var name: String? = null,
        @field:Attribute(name = "bayesaverage", required = false)
        var bayesaverage: String? = null,
        @field:Attribute(name = "type", required = false)
        var type: String? = null,
        @field:Attribute(name = "value", required = false)
        var value: String? = null,
        @field:Attribute(name = "id", required = false)
        var id: String? = null
)
