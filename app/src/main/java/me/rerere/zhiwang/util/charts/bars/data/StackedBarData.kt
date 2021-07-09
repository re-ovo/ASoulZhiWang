package me.rerere.zhiwang.util.charts.bars.data

import androidx.compose.ui.text.AnnotatedString

data class StackedBarData(
  val title: AnnotatedString,
  val entries: List<StackedBarEntry>,
) {
  val count get() = entries.sumBy { it.value.toInt() }
}
