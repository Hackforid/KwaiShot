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

        const val HOST = "http://baobab.kaiyanapp.com"
        const val INIT_URL = "$HOST/api/v4/tabs/selected"

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

    private var mNextUrl : String? = INIT_URL

    override fun getPage(page: Int): Observable<List<VideoInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nextPage(init: Boolean): Observable<List<VideoInfo>> {
        if (init) {
            mNextUrl = INIT_URL
        }
        return if (mNextUrl.isNullOrEmpty()) {
            Observable.just(ArrayList())
        } else {
            service.getHotVideos(mNextUrl!!)
                    .doOnNext {
                        mNextUrl = it.nextPageUrl
                    }
                    .map {
                        it.itemList.filter { it.type == "video" }
                    }
                    .map { list ->
                        list.map{ it.data.toCommonVideoInfo() }
                    }
        }
    }
}
