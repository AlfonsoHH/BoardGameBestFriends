package com.example.alfonsohernandez.boardgamebestfriends.domain.models

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("gameId")
	val gameId: Int? = null,

	@field:SerializedName("wantToPlay")
	val wantToPlay: Boolean? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("maxPlayers")
	val maxPlayers: Int? = null,

	@field:SerializedName("minPlayers")
	val minPlayers: Int? = null,

	@field:SerializedName("playingTime")
	val playingTime: Int? = null,

	@field:SerializedName("want")
	val want: Boolean? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("yearPublished")
	val yearPublished: Int? = null,

	@field:SerializedName("numPlays")
	val numPlays: Int? = null,

	@field:SerializedName("wantToBuy")
	val wantToBuy: Boolean? = null,

	@field:SerializedName("previousOwned")
	val previousOwned: Boolean? = null,

	@field:SerializedName("wishList")
	val wishList: Boolean? = null,

	@field:SerializedName("forTrade")
	val forTrade: Boolean? = null,

	@field:SerializedName("userComment")
	val userComment: String? = null,

	@field:SerializedName("owned")
	val owned: Boolean? = null,

	@field:SerializedName("bggRating")
	val bggRating: Double? = null,

	@field:SerializedName("averageRating")
	val averageRating: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("isExpansion")
	val isExpansion: Boolean? = null,

	@field:SerializedName("preOrdered")
	val preOrdered: Boolean? = null
)