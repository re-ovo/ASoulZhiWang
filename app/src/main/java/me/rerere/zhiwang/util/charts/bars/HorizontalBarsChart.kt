package me.rerere.zhiwang.util.charts.bars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.rerere.zhiwang.util.charts.bars.data.HorizontalBarsData
import me.rerere.zhiwang.util.charts.bars.data.StackedBarData
import me.rerere.zhiwang.util.charts.bars.data.StackedBarItem
import me.rerere.zhiwang.util.charts.internal.DefaultText
import me.rerere.zhiwang.util.charts.internal.safeGet
import me.rerere.zhiwang.util.charts.legend.DrawHorizontalLegend
import me.rerere.zhiwang.util.charts.legend.LegendEntry
import kotlin.math.min

internal val MinimumBarWidth = 24.dp
internal val OptionalBarOffset = 75.dp
internal val MaximumBarWidth = 275.dp

typealias TextRowFactory = @Composable RowScope.(title: AnnotatedString) -> Unit

internal val DropdownDefaultModifier = Modifier
  .requiredWidth(176.dp)
  .padding(16.dp)

@Composable
fun HorizontalBarsChart(
  data: HorizontalBarsData,
  modifier: Modifier = Modifier,
  divider: @Composable (() -> Unit)? = null,
  legend: @Composable (ColumnScope.(entries: List<LegendEntry>) -> Unit)? = null,
  legendOffset: Dp = 4.dp,
  dropdownModifier: Modifier = DropdownDefaultModifier,
  dropdownContent: @Composable (StackedBarData) -> Unit = {
    DropdownContent(data = it, colors = data.colors)
  },
  textContent: TextRowFactory = { DefaultText(text = it) },
  valueContent: TextRowFactory = { DefaultText(text = it) },
) {
  val legendEntries = remember(data) {
    data.customLegendEntries.takeIf { it.isNotEmpty() } ?: data.legendEntries()
  }
  var popupState: PopupState by remember { mutableStateOf(PopupState.Idle) }

  Column(modifier = modifier) {
    if (legend != null) {
      legend(legendEntries)
    } else {
      DrawHorizontalLegend(legendEntries = legendEntries)
    }

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(legendOffset)
    )

    val maxValue = data.bars.maxByOrNull { bar -> bar.count }?.count ?: 0

    data.bars.forEachIndexed { idx, bar ->
      Box {
        StackedHorizontalBar(
          modifier = Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            enabled = data.isPopupEnabled,
            onClick = { popupState = PopupState.Showing(bar) },
          ),
          colors = data.colors,
          data = bar,
          maxBarValue = maxValue,
          shouldDrawDivider = idx != data.bars.lastIndex,
          divider = divider,
          title = textContent,
          value = valueContent,
        )

        DropdownMenu(
          modifier = dropdownModifier,
          offset = DpOffset(0.dp, (-48).dp),
          expanded = popupState.let { it is PopupState.Showing && it.data == bar },
          onDismissRequest = { popupState = PopupState.Idle }
        ) {
          popupState.let {
            if (it is PopupState.Showing)
              dropdownContent(it.data)
          }
        }
      }
    }
  }
}

@Composable
internal fun StackedHorizontalBar(
  modifier: Modifier = Modifier,
  colors: List<Color>,
  data: StackedBarData,
  maxBarValue: Int,
  shouldDrawDivider: Boolean,
  divider: @Composable (() -> Unit)? = null,
  title: TextRowFactory = {},
  value: TextRowFactory = {},
) {
  val entries = data.entries.mapIndexed { idx, item ->
    StackedBarItem(
      text = item.text,
      value = item.value,
      color = item.color ?: colors.safeGet(idx)
    )
  }

  Column(modifier) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(12.dp)
    )
    Row(
      modifier = Modifier.heightIn(min = 24.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        // Trim the width of text to [container.width - 50.dp]
        Modifier.layout { measurable, constraints ->
          val width = constraints.maxWidth - 50.dp.roundToPx()
          val placeable = measurable.measure(constraints.copy(maxWidth = width, minWidth = width))
          layout(width, placeable.height) {
            placeable.placeRelative(0, 0)
          }
        }
      ) {
        title(data.title)
      }

      Spacer(
        modifier = Modifier.weight(1f)
      )

      value(AnnotatedString(data.count.toString()))
    }

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(4.dp)
    )

    BoxWithConstraints {
      val rightPadding = with(LocalDensity.current) { OptionalBarOffset.roundToPx() }
      val maxWidthPx = with(LocalDensity.current) { MaximumBarWidth.roundToPx() }
      val maxBarWidthPx = min(maxWidthPx, constraints.maxWidth - rightPadding)
      val minWidthPx = with(LocalDensity.current) { MinimumBarWidth.roundToPx() }

      val percentage = data.count / maxBarValue.toFloat()
      val width = maxBarWidthPx * percentage

      DrawStackedBar(
        entries = entries,
        widthPx = if (width < minWidthPx) width + minWidthPx else width
      )
    }

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(12.dp)
    )

    if (shouldDrawDivider && divider != null) {
      divider()
    }
  }
}
