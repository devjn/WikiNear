package com.github.devjn.wikinear.data

import com.google.gson.annotations.SerializedName

data class GeosearchItem(

	@field:SerializedName("ns")
	val ns: Int? = null,

	@field:SerializedName("dist")
	val dist: Double? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("pageid")
	val pageid: Int = 0,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("primary")
	val primary: String? = null
)