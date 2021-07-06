package me.rerere.zhiwang.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.math.PI

private val DarkColorPalette = darkColors(
    primary = Color(0xff05aa85),
    secondary = Color(0xffaa0529)
)

private val LightColorPalette = lightColors(
    primary = Color(0xff05aa85),
    secondary = Color(0xffaa0529)
)

@Composable
fun ZhiWangTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}