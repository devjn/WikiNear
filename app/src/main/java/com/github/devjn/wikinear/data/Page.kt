package com.github.devjn.wikinear.data

import com.google.gson.annotations.SerializedName


data class Page(

	@field:SerializedName("images")
	val images: List<ImagesItem>? = null,

	@field:SerializedName("ns")
	val ns: Int? = null,

	@field:SerializedName("pageid")
	val pageid: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)