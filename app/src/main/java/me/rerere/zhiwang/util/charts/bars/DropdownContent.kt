package me.rerere.zhiwang.util.charts.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.rerere.zhiwang.util.charts.bars.data.StackedBarData
import me.rerere.zhiwang.util.charts.bars.data.StackedBarItem

sealed class PopupState {
  object Idle : PopupState()
  data class Showing(val data: StackedBarData) : PopupState()
}

@Composable
internal fun DropdownContent(
  colors: List<Color> = emptyList(),
  data: StackedBarData,
) {
  val entries = remember(data) {
    data.entries.mapIndexed { idx, item ->
      StackedBarItem(
        text = item.text,
        value = item.value,
        color = item.color ?: colors[idx]
      )
    }
  }

  Text(
    data.title,
    style = MaterialTheme.typography.caption,
  )

  Spacer(
    modifier = Modifier
      .fillMaxWidth()
      .requiredHeight(8.dp)
  )

  entries.forEachIndexed { idx, (text, value, color) ->
    Row(Modifier.requiredHeight(20.dp), verticalAlignment = Alignment.CenterVertically) {
      Box(
        Modifier
          .requiredSize(8.dp)
          .background(color, CircleShape)
      )

      Text(
        text = text,
        modifier = Modifier.padding(start = 8.dp),
        style = MaterialTheme.typography.caption,
      )

      Spacer(
        modifier = Modifier
          .requiredHeight(20.dp)
          .weight(1f)
      )

      Text(
        text = value.toInt().toString(),
        style = MaterialTheme.typography.caption,
      )
    }

    if (idx != entries.lastIndex)
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .requiredHeight(8.dp)
      )
  }
}
