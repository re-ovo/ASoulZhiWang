package me.rerere.zhiwang.util

import kotlin.math.roundToInt

// format double
fun Double.format() = (this * 1000).roundToInt() / 1000.0
fun Double.formatToString() = format().toString()

// format float
fun Float.format() = (this * 1000).roundToInt() / 1000.0
fun Float.formatToString() = format().toString()