package me.rerere.zhiwang.api.zuowen


import com.google.gson.annotations.SerializedName

data class ZuowenContent(
    @SerializedName("htmlContent")
    val htmlContent: String
)