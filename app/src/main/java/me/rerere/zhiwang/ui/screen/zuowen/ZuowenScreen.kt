package me.rerere.zhiwang.ui.screen.zuowen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.title
import me.rerere.zhiwang.ui.public.FullScreenTopBar
import me.rerere.zhiwang.util.MEMBERS
import me.rerere.zhiwang.util.replaceASoulMemberName

private const val TAG = "ZuowenScreen"

@Composable
fun ZuowenScreen(
    navController: NavController,
    id: String,
    title: String,
    author: String,
    tags: List<String>,
    zuowenViewModel: ZuowenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Log.i(TAG, "ZuowenScreen: Load = $id, $title, $author")
        zuowenViewModel.load(id, title, author)
    }
    val replaceDialog = remember {
        MaterialDialog()
    }
    var replaceSelection by remember {
        mutableStateOf(0)
    }
    replaceDialog.build {
        title("快速替换小作文的主语并复制到剪贴板")
        listItemsSingleChoice(
            list = MEMBERS,
            initialSelection = replaceSelection,
            onChoiceChange = {
                replaceSelection = it
                println("select: $it")
            },
            waitForPositiveButton = false
        )
        buttons {
            button("复制") {
                replaceDialog.hide()
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(
                        null,
                        zuowenViewModel.content.replaceASoulMemberName(tags[0], MEMBERS[replaceSelection])
                    )
                )
                Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            FullScreenTopBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                title = {
                    Text(text = "阅读小作文")
                },
                actions = {
                    // 直接复制小作文
                    IconButton(onClick = {
                        if (!zuowenViewModel.error && !zuowenViewModel.loading) {
                            val clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboardManager.setPrimaryClip(
                                ClipData.newPlainText(
                                    null, zuowenViewModel.content
                                )
                            )
                            Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.ContentCopy, null)
                    }

                    if (tags.size == 1) {
                        // 小作文主语替换复制
                        IconButton(onClick = {
                            replaceDialog.show()
                        }) {
                            Icon(Icons.Default.FindReplace, null)
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .navigationBarsPadding()
        ) {
            ZuowenContent(zuowenViewModel)
        }
    }
}

@Composable
private fun ZuowenContent(zuowenViewModel: ZuowenViewModel) {
    when {
        // 加载中
        zuowenViewModel.loading -> {
            Column(Modifier.fillMaxWidth()) {
                repeat(8) {
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
        // 加载错误
        zuowenViewModel.error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "加载失败 😨", fontWeight = FontWeight.Bold)
            }
        }
        // 加载完成
        else -> {
            ArticleContent(zuowenViewModel)
        }
    }
}

@Composable
private fun ArticleContent(zuowenViewModel: ZuowenViewModel) {
    Column(
        Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = zuowenViewModel.title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text(text = zuowenViewModel.author)
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.LightGray)
        )
        // 显示作文内容
        SelectionContainer {
            Column {
                zuowenViewModel.content.split("\n").forEach {
                    Box(Modifier.padding(vertical = 4.dp)) {
                        Text(text = "  $it")
                    }
                }
            }
        }
    }
}