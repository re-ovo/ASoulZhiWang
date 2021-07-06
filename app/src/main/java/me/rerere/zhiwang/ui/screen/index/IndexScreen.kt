package me.rerere.zhiwang.ui.screen.index

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.rerere.zhiwang.ui.public.FullScreenTopBar
import me.rerere.zhiwang.ui.screen.index.page.Content
import me.rerere.zhiwang.ui.screen.index.page.Zuowen

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun IndexScreen(
    navController: NavController,
    indexScreenVideoModel: IndexScreenVideoModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val pager = rememberPagerState(pageCount = 3, initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomNavigation(modifier = Modifier.navigationBarsPadding()) {
                BottomNavigationItem(
                    selected = pager.currentPage == 0,
                    onClick = { coroutineScope.launch { pager.animateScrollToPage(0) } },
                    icon = {
                        Icon(Icons.Default.FindInPage, null)
                    }, label = {
                        Text(text = "小作文查重")
                    })
                BottomNavigationItem(
                    selected = pager.currentPage == 1,
                    onClick = { coroutineScope.launch { pager.animateScrollToPage(1) } },
                    icon = {
                        Icon(Icons.Default.Public, null)
                    }, label = {
                        Text(text = "优秀小作文")
                    })
                BottomNavigationItem(
                    selected = pager.currentPage == 2,
                    onClick = { coroutineScope.launch { pager.animateScrollToPage(2) } },
                    icon = {
                        Icon(Icons.Default.Info, null)
                    }, label = {
                        Text(text = "关于")
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
                            Zuowen(indexScreenVideoModel = indexScreenVideoModel)
                        }
                    }
                    2 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(text = "还没写")
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TopBar() {
    FullScreenTopBar(
        title = {
            Text(text = "ASoul小作文助手")
        }
    )
}