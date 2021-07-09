package me.rerere.zhiwang.util.charts.table

import androidx.compose.ui.text.AnnotatedString
import me.rerere.zhiwang.util.charts.ChartShape

data class TableEntry(
    val key: AnnotatedString?,
    val value: AnnotatedString?,
    val drawShape: ChartShape? = null,
)
