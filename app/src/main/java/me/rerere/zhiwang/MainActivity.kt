package me.rerere.zhiwang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.WindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.rerere.zhiwang.ui.screen.index.IndexScreen
import me.rerere.zhiwang.ui.theme.ZhiWangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 全屏
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ZhiWangTheme {
                ProvideWindowInsets {
                    val navController = rememberNavController()

                    val systemUiController = rememberSystemUiController()
                    val primaryColor = MaterialTheme.colors.primarySurface

                    // set ui color
                    SideEffect {
                        systemUiController.setNavigationBarColor(primaryColor)
                        systemUiController.setStatusBarColor(Color.Transparent, false)
                    }

                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = "index"
                    ) {
                        composable("index") {
                            IndexScreen()
                        }
                    }
                }
            }
        }
    }
}