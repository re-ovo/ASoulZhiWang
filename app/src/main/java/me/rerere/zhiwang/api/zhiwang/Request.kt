package me.rerere.zhiwang.api.zhiwang

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("text")
    val text: String
)