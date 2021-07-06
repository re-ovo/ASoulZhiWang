package me.rerere.zhiwang.ui.screen.index.page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.rerere.zhiwang.ui.public.XiaoZuoWen
import me.rerere.zhiwang.ui.screen.index.IndexScreenVideoModel
import me.rerere.zhiwang.util.formatToString
import me.rerere.zhiwang.util.getClipboardContent
import me.rerere.zhiwang.util.noRippleClickable

@ExperimentalAnimationApi
@Composable
fun Content(indexScreenVideoModel: IndexScreenVideoModel, scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    val response by indexScreenVideoModel.queryResult.observeAsState()
    var error by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    // 返回处理
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
                    .let {
                        if (response == null) {
                            it.height(200.dp)
                        } else {
                            it.wrapContentHeight()
                        }
                    }
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
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(5.dp),
                isError = error,
                maxLines = if (response == null) 8 else 1
            )
            // 输入框清空
            Row(
                modifier = Modifier
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(modifier = Modifier.noRippleClickable {
                    val text = context.getClipboardContent()
                    text?.let {
                        indexScreenVideoModel.content = it
                    } ?: kotlin.run {
                        Toast.makeText(context, "剪贴板没有内容", Toast.LENGTH_SHORT).show()
                    }
                }, imageVector = Icons.Default.ContentPaste, contentDescription = null)

                Spacer(modifier = Modifier.width(4.dp))

                androidx.compose.animation.AnimatedVisibility(visible = indexScreenVideoModel.content.isNotEmpty()) {
                    Icon(modifier = Modifier.noRippleClickable {
                        indexScreenVideoModel.content = ""
                        indexScreenVideoModel.queryResult.value = null
                    }, imageVector = Icons.Default.Clear, contentDescription = null)
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
            Text(text = "立即查重捏 🤤")
        }

        // 加载动画
        if (indexScreenVideoModel.loading) {
            val width = listOf(0.9f, 1f, 0.87f, 0.83f, 0.89f)
            repeat(5) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(width[it])
                        .height(90.dp)
                        .padding(16.dp)
                        .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
                )
            }
        }

        if (indexScreenVideoModel.error) {
            Text(text = "加载失败！😨", fontWeight = FontWeight.Bold)
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
                                    modifier = Modifier.padding(4.dp),
                                    color = MaterialTheme.colors.secondary
                                )
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .size(25.dp),
                                    progress = it.data.rate.toFloat()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboardManager.setPrimaryClip(
                                    ClipData.newPlainText(
                                        null, """
                                    查重结果:
                                    * 重复率: ${(it.data.rate * 100).formatToString()}%
                                    * 首次出现于: ${if (it.data.related.isNotEmpty()) it.data.related[0][2] else "无"}
                                    数据来源于枝网，仅供参考
                                """.trimIndent()
                                    )
                                )
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

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.Gray)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "数据来源于: https://asoulcnki.asia/",
            textAlign = TextAlign.Center
        )
    }
}