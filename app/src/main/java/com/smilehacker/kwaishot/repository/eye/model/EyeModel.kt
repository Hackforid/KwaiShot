package com.smilehacker.kwaishot.repository.eye.model

import com.google.gson.annotations.SerializedName
import com.smilehacker.kwaishot.repository.model.VideoInfo

/**
 * Created by quan.zhou on 2018/6/14.
 */

data class VideosResp(
        @SerializedName("itemList")
        val itemList: List<Item>,
        @SerializedName("nextPageUrl")
        val nextPageUrl: String?

) {
    data class Item(val type: String, val data: Video)
}

data class Video(
        val id: Long,
        val title: String,
        val description: String?,
        val author: Author?,
        val cover: Cover?,
        @SerializedName("playInfo")
        val playInfo: List<PlayInfo>

) {
    data class Author(
            val id: Long,
            val icon: String?,
            val name: String,
            val description: String?
    )

    data class Cover(
            @SerializedName("feed")
            val normal: String?,
            @SerializedName("homepage")
            val big: String?
    )

    data class PlayInfo(
            val height: Int,
            val width: Int,
            @SerializedName("urlList")
            val urlList: List<CDNUrl> // 刚想说API写的不错，看到个驼峰法命名
    ) {
        data class CDNUrl(
                val name: String,
                val url: String,
                val size: Long
        )
    }

}

fun Video.toCommonVideoInfo() : VideoInfo {
    val author = if (this.author != null) com.smilehacker.kwaishot.repository.model.Author(this.author.id, this.author.name, this.author.description, this.author.icon) else null
    val videos = this.playInfo.map {
        com.smilehacker.kwaishot.repository.model.Video(it.height, it.width,
                it.urlList.map {
                    url ->
                    com.smilehacker.kwaishot.repository.model.Video.VideoUrl(url.name, url.url, url.size)
                })
    }
    return VideoInfo(this.id, this.title, this.description,
            author, this.cover?.big, this.cover?.normal, videos)
}

