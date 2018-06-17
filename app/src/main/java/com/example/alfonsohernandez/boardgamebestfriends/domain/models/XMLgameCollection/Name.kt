package com.example.alfonsohernandez.boardgamebestfriends.domain.models.XMLgameCollection

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Name @JvmOverloads constructor(
		@field:Attribute(name = "sortindex",required = false)
		var sortindex: String? = null,
		@field:Element(name = "text",required = false)
		var text: String? = null
)
