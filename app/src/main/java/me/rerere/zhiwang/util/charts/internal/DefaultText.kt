package me.rerere.zhiwang.util.charts.internal

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString

@Composable
internal fun DefaultText(text: AnnotatedString?) {
  if (text != null)
    Text(text = text)
}
