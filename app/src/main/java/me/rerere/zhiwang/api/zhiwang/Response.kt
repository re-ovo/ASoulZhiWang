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
    )
}