package me.rerere.zhiwang.ui.screen.index.page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.rerere.zhiwang.ui.public.XiaoZuoWen
import me.rerere.zhiwang.ui.public.getDateTime
import me.rerere.zhiwang.ui.screen.index.IndexScreenVideoModel
import me.rerere.zhiwang.util.android.getClipboardContent
import me.rerere.zhiwang.util.format.format
import me.rerere.zhiwang.util.noRippleClickable

@ExperimentalAnimationApi
@Composable
fun Content(indexScreenVideoModel: IndexScreenVideoModel, scaffoldState: ScaffoldState) {
    // val coroutineScope = rememberCoroutineScope()
    val response by indexScreenVideoModel.queryResult.observeAsState()
    var error by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    // ????????????
    // ???????????????????????????????????????????????????????????????????????????????????????
    BackHandler(response != null) {
        indexScreenVideoModel.resetResult()
    }

    // ????????????
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // ?????????
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
                    Text(text = "???????????????????????????, ??????10?????????")
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(5.dp),
                isError = error,
                maxLines = if (response == null) 8 else 1
            )

            // ?????????????????????
            Row(
                modifier = Modifier
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ??????????????????
                androidx.compose.animation.AnimatedVisibility(visible = indexScreenVideoModel.queryResult.value == null) {
                    Icon(modifier = Modifier.noRippleClickable {
                        val text = context.getClipboardContent()
                        text?.let {
                            indexScreenVideoModel.content = it
                        } ?: kotlin.run {
                            Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show()
                        }
                    }, imageVector = Icons.Default.ContentPaste, contentDescription = null)
                }

                Spacer(modifier = Modifier.width(4.dp))

                // ??????
                androidx.compose.animation.AnimatedVisibility(visible = indexScreenVideoModel.content.isNotEmpty()) {
                    Icon(modifier = Modifier.noRippleClickable {
                        indexScreenVideoModel.content = ""
                        indexScreenVideoModel.queryResult.value = null
                    }, imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        }
        val focusManager = LocalFocusManager.current

        // ????????????
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            onClick = {
                focusManager.clearFocus()

                if (indexScreenVideoModel.content.length < 10) {
                    // ?????????????????????
                    error = true
                    Toast.makeText(context, "?????????????????????10?????????", Toast.LENGTH_SHORT).show()
                } else if (System.currentTimeMillis() - indexScreenVideoModel.lastQuery <= 5000L) {
                    Toast.makeText(context, "????????? 5 ??????????????????", Toast.LENGTH_SHORT).show()
                } else {
                    // ????????????
                    indexScreenVideoModel.resetResult()
                    indexScreenVideoModel.query()
                }
            }) {
            Text(text = "??????????????? ????")
        }

        // ????????????
        if (indexScreenVideoModel.loading) {
            val width = listOf(0.9f, 1f, 0.87f, 0.83f, 0.89f, 0.86f)
            repeat(6) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(width[it])
                        .height(90.dp)
                        .padding(16.dp)
                        .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
                )
            }
        }

        // ????????????
        if (indexScreenVideoModel.error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "???????????????????", fontWeight = FontWeight.Bold)
                    Text(text = "?????????????????????????????????????????????????????????????????????")
                }
            }
        }

        // ??????
        response?.let {
            when (it.code) {
                0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        //elevation = 4.dp
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
                                    text = "??????????????????: ${(it.data.rate * 100).format()}%",
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
                            // "??????????????????"??????
                            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboardManager.setPrimaryClip(
                                    ClipData.newPlainText(
                                        null, """
                                            ??????????????????????????????(APP???)
                                            ????????????: ${getDateTime(System.currentTimeMillis())}
                                            ??????????????????: ${(it.data.rate * 100).format()}%
                                            ???????????????: ${it.data.related[0].replyUrl}
                                            ??????: ${it.data.related[0].reply.mName}
                                            ????????????: ${getDateTime(it.data.related[0].reply.ctime.toLong() * 1000L)}

                                            ?????????????????????????????????????????????????????????
                                """.trimIndent()
                                    )
                                )
                                Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "????????????????????????")
                            }
                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                // ????????????????????????
                                Text(text = "?????????????????????????????????????????????????????????")
                                Text(text = "A??????????????????????????????")
                            }
                        }
                    }
                    // ??????????????????
                    Text(
                        text = "???????????????: (${it.data.related.size}???)",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                    // ?????????????????????

                    it.data.related.forEach { zuowen ->
                        XiaoZuoWen(zuowen)
                    }
                }
                4003 -> {
                    Text(text = "?????????????????????")
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
            text = "???????????????: https://asoulcnki.asia/",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
            textAlign = TextAlign.Center
        )
    }
}