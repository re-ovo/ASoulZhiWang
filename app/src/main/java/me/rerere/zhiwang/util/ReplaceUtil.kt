package me.rerere.zhiwang.util

val MEMBERS = listOf(
    "嘉然",
    "向晚",
    "乃琳",
    "珈乐",
    "贝拉"
)

fun String.replaceASoulMemberName(fromName: String, toName: String): String {
    return replace(fromName, toName)
}