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
        var related: List<List<Any>>,
        @SerializedName("start_time")
        val startTime: Int
    ){
        data class Zuowen(
            @SerializedName("content")
            val content: String,
            @SerializedName("ctime")
            val ctime: Int,
            @SerializedName("like_num")
            val likeNum: Int,
            @SerializedName("m_name")
            val mName: String,
            @SerializedName("mid")
            val mid: Int,
            @SerializedName("oid")
            val oid: Long,
            @SerializedName("rpid")
            val rpid: Long,
            @SerializedName("type_id")
            val typeId: Int
        )
    }
}