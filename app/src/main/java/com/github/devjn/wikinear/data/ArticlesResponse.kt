package com.github.devjn.wikinear.data

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(
	@field:SerializedName("batchcomplete")
	val batchcomplete: String? = null,

	@field:SerializedName("query")
	val query: Query = Query()
)

data class Query(
	@field:SerializedName("geosearch")
	val geosearch: List<GeosearchItem> = emptyList()
)