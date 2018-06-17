package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameCollection

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Status @JvmOverloads constructor(
        @field:Attribute(name = "wanttobuy", required = false)
        var wanttobuy: String? = null,
        @field:Attribute(name = "preordered", required = false)
        var preordered: String? = null,
        @field:Attribute(name = "lastmodified", required = false)
        var lastmodified: String? = null,
        @field:Attribute(name = "wanttoplay", required = false)
        var wanttoplay: String? = null,
        @field:Attribute(name = "own", required = false)
        var own: String? = null,
        @field:Attribute(name = "fortrade", required = false)
        var fortrade: String? = null,
        @field:Attribute(name = "wishlist", required = false)
        var wishlist: String? = null,
        @field:Attribute(name = "want", required = false)
        var want: String? = null,
        @field:Attribute(name = "prevowned", required = false)
        var prevowned: String? = null
)
