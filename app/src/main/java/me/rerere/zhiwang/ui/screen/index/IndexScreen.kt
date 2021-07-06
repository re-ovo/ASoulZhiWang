package me.rerere.zhiwang.ui.screen.index

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.coroutines.launch
import me.rerere.zhiwang.ui.public.FullScreenTopBar
import me.rerere.zhiwang.ui.public.XiaoZuoWen
import me.rerere.zhiwang.ui.screen.index.page.Content
import me.rerere.zhiwang.util.formatToString
import me.rerere.zhiwang.util.noRippleClickable

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
                            Text(text = "还没写")
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