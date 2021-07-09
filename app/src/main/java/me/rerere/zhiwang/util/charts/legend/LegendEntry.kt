package me.rerere.zhiwang.util.charts.legend

import androidx.compose.ui.text.AnnotatedString
import me.rerere.zhiwang.util.charts.ChartShape

data class LegendEntry(
  val text: AnnotatedString,
  val value: Float,
  val percent: Float = Float.MAX_VALUE,
  val shape: ChartShape = ChartShape.Default
)
