package me.rerere.zhiwang.api.bilibili


import com.google.gson.annotations.SerializedName

data class SubList(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
) {
    data class Data(
        @SerializedName("list")
        val list: List<User>,
        @SerializedName("re_version")
        val reVersion: Long,
        @SerializedName("total")
        val total: Int
    ) {
        data class User(
            @SerializedName("attribute")
            val attribute: Int,
            @SerializedName("face")
            val face: String,
            @SerializedName("mid")
            val mid: Int,
            @SerializedName("mtime")
            val mtime: Int,
            @SerializedName("sign")
            val sign: String,
            @SerializedName("special")
            val special: Int,
            @SerializedName("tag")
            val tag: Any,
            @SerializedName("uname")
            val uname: String,
        )
    }
}