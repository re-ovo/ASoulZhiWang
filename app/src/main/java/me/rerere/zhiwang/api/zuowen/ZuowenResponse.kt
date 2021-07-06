package me.rerere.zhiwang.api.zuowen


import com.google.gson.annotations.SerializedName

data class ZuowenResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("info")
    val info: String
) {
    data class Article(
        @SerializedName("author")
        val author: String,
        @SerializedName("_id")
        val id: String,
        @SerializedName("plainContent")
        val plainContent: String,
        @SerializedName("submissionTime")
        val submissionTime: Int,
        @SerializedName("tags")
        val tags: List<String>,
        @SerializedName("title")
        val title: String
    )
}