package me.rerere.zhiwang.util.charts.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import me.rerere.zhiwang.util.charts.internal.DefaultText

@Composable
fun DrawHorizontalLegend(
  legendEntries: List<LegendEntry>,
  text: @Composable (item: LegendEntry) -> Unit = { DefaultText(text = it.text) },
) {
  FlowRow(
    mainAxisSpacing = 16.dp,
    crossAxisSpacing = 8.dp,
  ) {
    legendEntries.forEachIndexed { _,item ->
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .requiredSize(item.shape.size)
            .background(item.shape.color, item.shape.shape)
        )

        Spacer(modifier = Modifier.requiredSize(8.dp))

        text(item)
      }
    }
  }
}
