package me.rerere.zhiwang.util.charts.bars

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import me.rerere.zhiwang.util.charts.ChartShape
import me.rerere.zhiwang.util.charts.bars.data.HorizontalBarsData
import me.rerere.zhiwang.util.charts.internal.safeGet
import me.rerere.zhiwang.util.charts.legend.LegendEntry

internal fun HorizontalBarsData.uniqueBarEntries() =
  bars.asSequence()
    .map { bar -> bar.entries }
    .flatten()
    .distinctBy { it.text }
    .toList()

internal fun HorizontalBarsData.legendEntries() = uniqueBarEntries().mapIndexed { idx, item ->
  LegendEntry(
    text = item.text,
    value = item.value,
    percent = 0f,
    shape = ChartShape(
      size = 8.dp,
      color = item.color ?: colors.safeGet(idx),
      shape = CircleShape,
    )
  )
}

internal enum class EntryDrawShape {
  // Rounded on all sides
  Single,

  // Rounded left side
  First,

  // Not rounded
  Middle,

  // Rounded right side
  Last;
}

// TODO: make this customisable
internal fun createBarEntryShape(shape: EntryDrawShape, size: Size): Path {
  val rect = Rect(Offset(0f, 0f), Offset(size.width, size.height))
  return Path().apply {
    when (shape) {
      EntryDrawShape.Single -> addRoundRect(
        RoundRect(
          rect = rect,
          cornerRadius = CornerRadius(size.height)
        )
      )
      EntryDrawShape.First -> addRoundRect(
        RoundRect(
          rect = rect,
          topRight = CornerRadius.Zero,
          bottomRight = CornerRadius.Zero,
          topLeft = CornerRadius(size.height),
          bottomLeft = CornerRadius(size.height),
        )
      )
      EntryDrawShape.Middle -> addRect(rect = rect)
      EntryDrawShape.Last -> addRoundRect(
        RoundRect(
          rect = rect,
          topLeft = CornerRadius.Zero,
          bottomLeft = CornerRadius.Zero,
          topRight = CornerRadius(size.height),
          bottomRight = CornerRadius(size.height),
        )
      )
    }
  }
}
