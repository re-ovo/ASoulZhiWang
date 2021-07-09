package me.rerere.zhiwang.api.bilibili

data class SubResult(
    val id: String,
    val name: String,
    val subList: Set<SubUser>
)

data class SubUser(
    val mid: Int,
    val name: String
)