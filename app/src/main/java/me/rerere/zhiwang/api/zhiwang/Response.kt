package me.rerere.zhiwang.api.zhiwang


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("end_time")
        val endTime: Int,
        @SerializedName("rate")
        val rate: Double,
        @SerializedName("related")
        val related: List<Related>,
        @SerializedName("start_time")
        val startTime: Int
    ) {
        data class Related(
            @SerializedName("rate")
            val rate: Double,
            @SerializedName("reply")
            val reply: Reply,
            @SerializedName("reply_url")
            val replyUrl: String
        ) {
            data class Reply(
                @SerializedName("content")
                val content: String,
                @SerializedName("ctime")
                val ctime: Int,
                @SerializedName("dynamic_id")
                val dynamicId: String,
                @SerializedName("like_num")
                val likeNum: Int,
                @SerializedName("m_name")
                val mName: String,
                @SerializedName("mid")
                val mid: Int,
                @SerializedName("oid")
                val oid: String,
                @SerializedName("origin_rpid")
                val originRpid: String,
                @SerializedName("rpid")
                val rpid: String,
                @SerializedName("similar_count")
                val similarCount: Int,
                @SerializedName("similar_like_sum")
                val similarLikeSum: Int,
                @SerializedName("type_id")
                val typeId: Int,

                var avatar: String = ""
            )
        }
    }
}