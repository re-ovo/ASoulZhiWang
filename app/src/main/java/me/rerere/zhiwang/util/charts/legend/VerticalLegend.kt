package me.rerere.zhiwang.util.charts.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.rerere.zhiwang.util.charts.internal.DefaultText

@Composable
fun RowScope.DrawVerticalLegend(
  legendEntries: List<LegendEntry>,
  text: @Composable (entry: LegendEntry) -> Unit = {
    DefaultText(text = it.text)
  },
) {
  Column(
    modifier = Modifier.weight(1f),
    verticalArrangement = Arrangement.Center
  ) {
    legendEntries.forEachIndexed { idx, item ->
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .requiredSize(item.shape.size)
            .background(item.shape.color, item.shape.shape)
        )

        Spacer(modifier = Modifier.requiredSize(8.dp))

        text(item)
      }

      if (idx != legendEntries.lastIndex)
        Spacer(modifier = Modifier.requiredSize(8.dp))
    }
  }
}
