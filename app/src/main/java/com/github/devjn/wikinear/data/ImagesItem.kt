package com.github.devjn.wikinear.data

import com.google.gson.annotations.SerializedName

data class ImagesItem(

	@field:SerializedName("ns")
	val ns: Int? = null,

	@field:SerializedName("title")
	val title: String = ""
)