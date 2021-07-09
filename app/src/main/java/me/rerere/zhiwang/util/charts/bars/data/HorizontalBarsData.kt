package me.rerere.zhiwang.util.charts.bars.data

import androidx.compose.ui.graphics.Color
import me.rerere.zhiwang.util.charts.legend.LegendEntry

data class HorizontalBarsData(
  /**
   * List of horizontal bars to be drawn
   */
  val bars: List<StackedBarData>,
  /**
   * Optional
   *
   * Colors for every bar item
   */
  val colors: List<Color> = emptyList(),
  /**
   * Optional. Items specified here will replace items inferred from [bars]
   */
  val customLegendEntries: List<LegendEntry> = emptyList(),
  /**
   * Whether to enabled popup on bar click or not
   */
  val isPopupEnabled: Boolean = true,
)
