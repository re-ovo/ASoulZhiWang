package me.rerere.zhiwang.ui.screen.index.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.rerere.zhiwang.api.zuowen.ZuowenResponse
import me.rerere.zhiwang.ui.screen.index.IndexScreenVideoModel
import me.rerere.zhiwang.util.noRippleClickable
import java.lang.StringBuilder

@Composable
fun Zuowen(indexScreenVideoModel: IndexScreenVideoModel, navController: NavController) {
    val articleList = indexScreenVideoModel.pager.collectAsLazyPagingItems()
    SwipeRefresh(
        state = rememberSwipeRefreshState(articleList.loadState.refresh == LoadState.Loading),
        onRefresh = { articleList.refresh() }) {
        LazyColumn(Modifier.fillMaxSize()) {
            if(articleList.loadState.refresh is LoadState.Error) {
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "加载失败，下拉重试 😱")
                    }
                }
            }

            if(articleList.loadState.refresh == LoadState.Loading && articleList.itemCount == 0){
                repeat(10){
                    item {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .height(70.dp)
                                .placeholder(visible = true, highlight = PlaceholderHighlight.fade())
                        )
                    }
                }
            }

            items(articleList) {
                Article(it!!, navController)
            }

            when (articleList.loadState.append) {
                LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is LoadState.Error -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable {
                                    articleList.retry()
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(text = "加载更多页面失败，点击重试！", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun Article(article: ZuowenResponse.Article, navController: NavController) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("zuowen?id=${article.id}&title=${article.title}&author=${article.author}&tags=${article.tags.joinToString(",")}")
            },
        elevation = 4.dp
    ) {
        Column(Modifier.padding(16.dp)) {
            // 标题
            Text(text = article.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            // 作者
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = article.author)
            }
            // 分割线
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color.LightGray)
            )
            // 内容
            Text(text = article.plainContent)
            // 分割线
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color.LightGray)
            )
            // 标签
            Row {
                article.tags.forEach {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                    )
                }
            }
        }
    }
}