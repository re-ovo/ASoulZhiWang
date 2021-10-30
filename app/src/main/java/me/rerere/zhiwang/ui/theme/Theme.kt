package me.rerere.zhiwang.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColors(
    primary = Color(0xff05aa85),
    secondary = Color(0xffaa0529)
)

private val LightColorPalette = lightColors(
    primary = Color(0xff05aa85),
    secondary = Color(0xffaa0529)
)

val Colors.uiBackGroundColor
    get() = if(isLight){
        Color.White
    } else {
        Color.Black
    }

@Composable
fun md3Color(
    darkTheme: Boolean
): ColorScheme {
    val context = LocalContext.current
    return if (darkTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicDarkColorScheme(context)
        } else {
            darkColorScheme()
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicLightColorScheme(context)
        }else {
            lightColorScheme()
        }
    }
}

@Composable
fun ZhiWangTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    androidx.compose.material3.MaterialTheme(
        colorScheme = md3Color(darkTheme)
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}