package me.rerere.zhiwang.ui.screen.index

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.rerere.zhiwang.ui.public.FullScreenTopBar
import me.rerere.zhiwang.ui.public.XiaoZuoWen
import me.rerere.zhiwang.util.formatToString
import me.rerere.zhiwang.util.noRippleClickable

@ExperimentalAnimationApi
@Composable
fun IndexScreen(
    navController: NavController,
    indexScreenVideoModel: IndexScreenVideoModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar()
        }
    ) {
        Content(indexScreenVideoModel = indexScreenVideoModel, scaffoldState = scaffoldState)
    }
}

@ExperimentalAnimationApi
@Composable
private fun Content(indexScreenVideoModel: IndexScreenVideoModel, scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    val response by indexScreenVideoModel.queryResult.observeAsState()
    var error by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    BackHandler(response != null) {
        indexScreenVideoModel.resetResult()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        // 输入框
        Box(contentAlignment = Alignment.BottomEnd) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .height(if (response == null) 200.dp else 100.dp)
                    .padding(16.dp),
                value = indexScreenVideoModel.content,
                onValueChange = {
                    if (it.length >= 10) {
                        error = false
                    }
                    indexScreenVideoModel.content = it
                },
                label = {
                    Text(text = "输入要查重的小作文, 至少10个字哦")
                },
                colors = textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(5.dp),
                isError = error
            )
            // 输入框清空
            androidx.compose.animation.AnimatedVisibility(visible = indexScreenVideoModel.content.isNotEmpty()) {
                Box(modifier = Modifier
                    .padding(16.dp)
                    .noRippleClickable {
                        indexScreenVideoModel.content = ""
                        indexScreenVideoModel.queryResult.value = null
                    }) {
                    Icon(Icons.Default.Clear, null)
                }
            }
        }
        val focusManager = LocalFocusManager.current
        // 查重按钮
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            onClick = {
                focusManager.clearFocus()

                if (indexScreenVideoModel.content.length < 10) {
                    // 小作文长度不够
                    error = true
                    Toast.makeText(context, "小作文至少需要10个字哦", Toast.LENGTH_SHORT).show()
                } else {
                    // 开始查询
                    indexScreenVideoModel.resetResult()
                    indexScreenVideoModel.query()
                }
            }) {
            Text(text = "立即查重捏")
        }

        // 加载动画
        if (indexScreenVideoModel.loading) {
            val width = listOf(0.9f, 1f, 0.87f, 0.83f, 0.89f)
            repeat(5){
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(width[it])
                        .height(90.dp)
                        .padding(16.dp)
                        .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
                )
            }
        }

        // 结果
        response?.let {
            when (it.code) {
                0 -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "总文字复制比: ${(it.data.rate * 100).formatToString()}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 23.sp,
                                    modifier = Modifier.padding(4.dp)
                                )
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .size(25.dp),
                                    progress = it.data.rate.toFloat()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(onClick = {
                                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, """
                                    查重结果:
                                    * 重复率: ${(it.data.rate * 100).formatToString()}%
                                    * 首次出现于: ${if(it.data.related.isNotEmpty()) it.data.related[0][2] else "无"}
                                    数据来源于枝网，仅供参考
                                """.trimIndent()))
                                Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "点击复制查重结果")
                            }
                        }
                    }
                    Text(
                        text = "相似小作文: (${it.data.related.size}篇)",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                    LazyColumn(Modifier.fillMaxWidth()) {
                        items(it.data.related) { zuowen ->
                            XiaoZuoWen(zuowen)
                        }
                    }
                }
                4003 -> {
                    Text(text = "服务器内部错误")
                }
            }
        }
    }
}

@Composable
private fun TopBar() {
    FullScreenTopBar(
        title = {
            Text(text = "枝网查重")
        }
    )
}