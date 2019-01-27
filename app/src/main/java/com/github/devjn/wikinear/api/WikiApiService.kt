package com.github.devjn.wikinear.api

import com.github.devjn.wikinear.data.ArticlesResponse
import com.github.devjn.wikinear.data.ImagesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by @author Jahongir on 26-Jan-19
 * devjn@jn-arts.com
 * WikiService
 */

interface WikiApiService {

    @GET("api.php?action=query&list=geosearch&gsradius=10000&gscoord=gscoord&gslimit=50&format=json")
    fun getArticlesByCoordinates(
        @Query("gscoord") gscoord: String
    ): Single<ArticlesResponse>

    @GET("api.php?action=query&prop=images&pageids=pageids&format=json")
    fun getArticleImages(@Query("pageids") articleId: Int): Single<ImagesResponse>

}