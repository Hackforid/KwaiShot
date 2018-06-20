package com.smilehacker.kwaishot.repository.eye

import com.smilehacker.kwaishot.repository.DataProvider
import com.smilehacker.kwaishot.repository.eye.model.toCommonVideoInfo
import com.smilehacker.kwaishot.repository.model.VideoInfo
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by quan.zhou on 2018/6/14.
 */
class EyeVideoRepository : DataProvider {

    companion object {

        val service: EyeService by lazy { buildService() }

        private fun buildService() : EyeService {
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://baobab.kaiyanapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(EyeService::class.java)
        }
    }

    override fun getPage(page: Int) : Observable<List<VideoInfo>> {
        return service.getVideos(page = page)
                .map {
                    it.itemList.filter { it.type == "video" }
                }
                .map { list ->
                    list.map{ it.data.toCommonVideoInfo() }
                }
    }

}
