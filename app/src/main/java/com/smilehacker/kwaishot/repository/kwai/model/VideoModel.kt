package com.smilehacker.kwaishot.repository.kwai.model

import com.google.gson.annotations.SerializedName

data class VideoModel(
        val feeds: List<Feed>
)

data class Feed(
        @SerializedName("photo_id") val id: Long,
        @SerializedName("user_id") val userID: Long,
        @SerializedName("caption") val desc: String?,
        val timestamp: Long,
        val likeCount: Int,
        val viewCount: Int,
        val forwardCount: Int,
        val commentCount: Int,
        val userName: String?,
        val coverWebpUrls: List<CDNUrl>,
        @SerializedName("headurls") val headUrls: List<CDNUrl>,
        val extParams: ExtParams
        ) {
        data class ExtParams(
                val w: Int,
                val h: Int
        )
}

data class CDNUrl(
        val cdn: String,
        val url: String
)

