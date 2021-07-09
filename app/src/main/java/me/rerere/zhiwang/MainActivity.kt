package me.rerere.zhiwang

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import me.rerere.zhiwang.ui.screen.index.IndexScreen
import me.rerere.zhiwang.ui.screen.zuowen.ZuowenScreen
import me.rerere.zhiwang.ui.theme.ZhiWangTheme
import me.rerere.zhiwang.ui.theme.uiBackGroundColor

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
                    val primaryColor = MaterialTheme.colors.uiBackGroundColor
                    val darkIcons = MaterialTheme.colors.isLight

                    // 设置状态栏和导航栏颜色
                    SideEffect {
                        systemUiController.setNavigationBarColor(primaryColor)
                        systemUiController.setStatusBarColor(
                            Color.Transparent,
                            darkIcons = darkIcons
                        )
                    }

                    // 导航部件
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = "index"
                    ) {
                        composable("index") {
                            IndexScreen(navController)
                        }

                        composable("zuowen?id={id}&title={title}&author={author}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.StringType
                                },
                                navArgument("title") {
                                    type = NavType.StringType
                                },
                                navArgument("author") {
                                    type = NavType.StringType
                                }
                            )) {
                            ZuowenScreen(
                                navController,
                                it.arguments?.getString("id")!!,
                                it.arguments?.getString("title")!!,
                                it.arguments?.getString("author")!!,
                            )
                        }
                    }
                }
            }
        }

        // 禁止强制暗色模式，因为已经适配了夜间模式，所以不需要强制反色
        // 国产UI似乎必需这样做(isForceDarkAllowed = false)才能阻止反色，原生会自动识别
        val existingComposeView = window.decorView
            .findViewById<ViewGroup>(android.R.id.content)
            .getChildAt(0) as? ComposeView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            existingComposeView?.isForceDarkAllowed = false
        }
    }
}