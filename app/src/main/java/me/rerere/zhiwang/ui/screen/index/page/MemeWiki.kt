package me.rerere.zhiwang.ui.screen.index.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.rerere.zhiwang.api.wiki.WikiList
import me.rerere.zhiwang.ui.screen.index.IndexScreenVideoModel
import me.rerere.zhiwang.util.noRippleClickable

@Composable
fun WikiPage(navController: NavController, indexViewModel: IndexScreenVideoModel) {
    when {
        indexViewModel.wikiLoading -> {
            Column {
                repeat(7) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(70.dp)
                            .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
                    )
                }
            }
        }
        indexViewModel.wikiError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "加载失败，点击重新加载",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.noRippleClickable {
                            indexViewModel.loadWiki()
                        })
                }
            }
        }
        else -> {
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = indexViewModel.wikiLoading), onRefresh = { indexViewModel.loadWiki() }) {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(text = "声明", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "本WIKI中许多梗介绍来自  深紫色的白")
                                Text(text = "如果你想添加梗介绍，请前往github提交对 wiki.json 的PR")
                            }
                        }
                    }
                    items(indexViewModel.wikiList){
                        WikiItem(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun WikiItem(wikiListItem: WikiList.WikiListItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = wikiListItem.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = wikiListItem.description)
        }
    }
}