package com.smilehacker.kwaishot.repository.model

/**
 * Created by quan.zhou on 2018/6/14.
 */
data class VideoInfo(
        val id: Long,
        val title: String,
        val desc: String?,

        val author: Author,

        val bigCover: String?,
        val normalCover: String?,

        val videos: List<Video>
)


data class Video(
        val height: Int,
        val width: Int,
        val urls: List<VideoUrl>
) {
    data class VideoUrl(
            val sourceName: String,
            val url: String,
            val size: Long
    )
}

data class Author(
        val id: Long,
        val name: String,
        val desc: String?,
        val icon: String?
)
