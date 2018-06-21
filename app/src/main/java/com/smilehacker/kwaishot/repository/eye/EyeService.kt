package com.smilehacker.kwaishot.repository.eye

import com.smilehacker.kwaishot.repository.eye.model.VideosResp
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by quan.zhou on 2018/6/14.
 */
interface EyeService {
    // todo http://baobab.kaiyanapp.com/api/v4/tabs/selected?date=1529197200000&num=2&page=4
    @GET("api/v4/tabs/selected")
    fun getVideos(@Query("num") num: Int = 2, @Query("page") page: Int) : Observable<VideosResp>
}