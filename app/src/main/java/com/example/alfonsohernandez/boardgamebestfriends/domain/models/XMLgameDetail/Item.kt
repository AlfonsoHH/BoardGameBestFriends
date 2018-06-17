package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.*

@Root(strict = false)
data class Item @JvmOverloads constructor(
        @field:Element(name = "image",required = false)
        var image: String? = null,
        @field:Element(name = "thumbnail",required = false)
        var thumbnail: String? = null,
        @field:Element(name = "minplaytime",required = false)
        var minplaytime: Minplaytime? = null,
        @field:ElementList(entry = "link", inline = true,required = false)
        var link: List<Linkitem?>? = null,
        @field:Attribute(name = "type",required = false)
        var type: String? = null,
        @field:Element(name = "description",required = false)
        var description: String? = null,
        @field:ElementList(entry = "poll", inline = true,required = false)
        var poll: List<Pollitem?>? = null,
        @field:Element(name = "minplayers",required = false)
        var minplayers: Minplayers? = null,
        @field:Element(name = "playingtime",required = false)
        var playingtime: Playingtime? = null,
        @field:Element(name = "minage",required = false)
        var minage: Minage? = null,
        @field:Element(name = "yearpublished",required = false)
        var yearpublished: Yearpublished? = null,
        @field:Element(name = "maxplaytime",required = false)
        var maxplaytime: Maxplaytime? = null,
        @field:ElementList(entry = "name", inline = true,required = false)
        var name: List<Nameitem?>? = null,
        @field:Element(name = "maxplayers",required = false)
        var maxplayers: Maxplayers? = null,
        @field:Element(name = "statistics",required = false)
        var statistics: Statistics? = null,
        @field:Attribute(name = "id",required = false)
        var id: String? = null
)
