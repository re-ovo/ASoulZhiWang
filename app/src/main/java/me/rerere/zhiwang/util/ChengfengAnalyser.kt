package me.rerere.zhiwang.util

import me.rerere.zhiwang.api.bilibili.SubUser

// ASOUL成员 MID
private val ASOUL_MID = listOf(
    703007996,
    351609538,
    672342685,
    672353429,
    672346917,
    672328094
)

// 原神有关的UP
private val GENSHEN_MID = listOf(
    401742377, // 原神官方
    318432901, // 米哈游
    19898, // 米哈游客服
)

fun Set<SubUser>.analyse(): Map<String, Float> {
    val userMap = hashMapOf<String, Int>()
    this.forEach {
        val mid = it.mid
        val name = it.name
        when {
            // 守护最好的ASOUL
            ASOUL_MID.contains(mid) -> {
                userMap + "ASOUL"
            }

            // 用official结尾的多半沾点VTuber
            name.endsWith("official", true) -> userMap + "VTuber"

            // 原神
            GENSHEN_MID.contains(mid) -> userMap + "原神"
        }
    }
    val total = userMap.values.sum()
    return userMap.mapValues { it.value.toFloat() / total }
}

operator fun MutableMap<String, Int>.plus(tag: String) {
    this[tag] = this[tag]?.plus(1) ?: 1
}