package me.rerere.zhiwang.ui.screen.index

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.rerere.zhiwang.R
import me.rerere.zhiwang.ui.public.FullScreenTopBar
import me.rerere.zhiwang.ui.screen.index.page.AboutPage
import me.rerere.zhiwang.ui.screen.index.page.ChengfenPage
import me.rerere.zhiwang.ui.screen.index.page.Content
import me.rerere.zhiwang.ui.screen.index.page.Zuowen
import me.rerere.zhiwang.ui.theme.uiBackGroundColor
import me.rerere.zhiwang.util.noRippleClickable

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun IndexScreen(
    navController: NavController,
    indexScreenVideoModel: IndexScreenVideoModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val pager = rememberPagerState(pageCount = 4, initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(indexScreenVideoModel)
        },
        bottomBar = {
            val pages = listOf(
                0 to "小作文查重",
                1 to "小作文库",
                2 to "查成分",
                3 to "关于"
            )

            BottomNavigation(modifier = Modifier.navigationBarsPadding(), backgroundColor = MaterialTheme.colors.uiBackGroundColor) {
                BottomNavigationItem(
                    selected = pager.currentPage == 0,
                    onClick = { coroutineScope.launch { pager.animateScrollToPage(0) } },
                    icon = {
                        Icon(Icons.Default.FindInPage, null)
                    }, label = {
                        Text(text = pages[0].second)
                    })
                BottomNavigationItem(
                    selected = pager.currentPage == 1,
                    onClick = { coroutineScope.launch { pager.animateScrollToPage(1) } },
                    icon = {
                        Icon(Icons.Default.Public, null)
                    }, label = {
                        Text(text = pages[1].second)
                    })
                BottomNavigationItem(
                    selected = pager.currentPage == 2,
                    onClick = { coroutineScope.launch { pager.animateScrollToPage(2) } },
                    icon = {
                        Icon(painterResource(R.drawable.chengfen), null)
                    }, label = {
                        Text(text = pages[2].second)
                    })
                BottomNavigationItem(
                    selected = pager.currentPage == 3,
                    onClick = { coroutineScope.launch { pager.animateScrollToPage(3) } },
                    icon = {
                        Icon(Icons.Default.Info, null)
                    }, label = {
                        Text(text = pages[3].second)
                    })
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            HorizontalPager(modifier = Modifier.fillMaxSize(), state = pager) {
                when (it) {
                    0 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Content(
                                indexScreenVideoModel = indexScreenVideoModel,
                                scaffoldState = scaffoldState
                            )
                        }
                    }
                    1 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Zuowen(
                                indexScreenVideoModel = indexScreenVideoModel,
                                navController = navController
                            )
                        }
                    }
                    2 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            ChengfenPage(indexScreenVideoModel)
                        }
                    }
                    3 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AboutPage()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TopBar(indexScreenVideoModel: IndexScreenVideoModel) {
    val context = LocalContext.current
    FullScreenTopBar(
        title = {
            Text(text = "ASoul小作文助手")
        },
        actions = {
            if (indexScreenVideoModel.foundUpdate) {
                Text(text = "APP有更新", modifier = Modifier
                    .noRippleClickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/jiangdashao/ASoulZhiWang/releases/latest")
                        )
                        context.startActivity(intent)
                    }
                    .padding(horizontal = 16.dp))
            }
        }
    )
}