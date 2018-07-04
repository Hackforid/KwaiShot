package com.smilehacker.kwaishot.repository.kwai

import retrofit2.http.GET
import retrofit2.http.Path

interface KwaiService {

    // host http://http://reload.kwai.net/

    @GET("oversea/hot/{bucket}_{page}.json")
    fun getHotFeed(@Path("bucket") bucket: String, @Path("page") page: Int)
}