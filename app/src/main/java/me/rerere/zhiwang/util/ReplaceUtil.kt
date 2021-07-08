package me.rerere.zhiwang.util

val MEMBERS = listOf(
    "嘉然",
    "向晚",
    "乃琳",
    "珈乐",
    "贝拉"
)

fun String.replaceASoulMemberName(toName: String): String {
    var text = this
    MEMBERS.forEach {
        text = text.replace(it, toName)
    }
    return text
}