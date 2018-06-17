package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Ratings @JvmOverloads constructor(
        @field:Element(name = "trading", required = false)
        var trading: Trading? = null,
        @field:Element(name = "usersrated", required = false)
        var usersrated: Usersrated? = null,
        @field:Element(name = "average", required = false)
        var average: Average? = null,
        @field:Element(name = "wanting", required = false)
        var wanting: Wanting? = null,
        @field:Element(name = "numcomments", required = false)
        var numcomments: Numcomments? = null,
        @field:Element(name = "wishing", required = false)
        var wishing: Wishing? = null,
        @field:Element(name = "averageweight", required = false)
        var averageweight: Averageweight? = null,
        @field:Element(name = "ranks", required = false)
        var ranks: Ranks? = null,
        @field:Element(name = "median", required = false)
        var median: Median? = null,
        @field:Element(name = "bayesaverage", required = false)
        var bayesaverage: Bayesaverage? = null,
        @field:Element(name = "owned", required = false)
        var owned: Owned? = null,
        @field:Element(name = "numweights", required = false)
        var numweights: Numweights? = null,
        @field:Element(name = "stddev", required = false)
        var stddev: Stddev? = null
)
