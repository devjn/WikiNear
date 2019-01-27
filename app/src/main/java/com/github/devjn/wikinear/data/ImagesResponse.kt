package com.github.devjn.wikinear.data

import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @field:SerializedName("query")
    val query: ImagesQuery = ImagesQuery()
)

data class ImagesQuery(
    @field:SerializedName("pages")
    val pages: Map<Int, Page> = emptyMap()
)