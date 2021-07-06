package me.rerere.zhiwang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import me.rerere.zhiwang.ui.screen.index.IndexScreen
import me.rerere.zhiwang.ui.theme.ZhiWangTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalPagerApi
    @ExperimentalAnimationApi
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
                    val darkIcons = MaterialTheme.colors.isLight

                    // set ui color
                    SideEffect {
                        systemUiController.setNavigationBarColor(primaryColor)
                        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = darkIcons)
                    }

                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = "index"
                    ) {
                        composable("index") {
                            IndexScreen(navController)
                        }
                    }
                }
            }
        }
    }
}