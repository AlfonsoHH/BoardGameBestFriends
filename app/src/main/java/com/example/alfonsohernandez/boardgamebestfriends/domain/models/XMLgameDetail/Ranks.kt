package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameDetail

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false)
data class Ranks @JvmOverloads constructor(
        @field:ElementList(entry = "rank", inline = true, required = false)
        var rank: List<RankItem?>? = null
)
