package me.rerere.zhiwang.ui.screen.index.page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.rerere.zhiwang.ui.screen.index.IndexScreenVideoModel
import me.rerere.zhiwang.util.analyse
import me.rerere.zhiwang.util.charts.pie.PieChart
import me.rerere.zhiwang.util.charts.pie.PieChartData
import me.rerere.zhiwang.util.charts.pie.PieChartEntry
import me.rerere.zhiwang.util.formatToString

@Composable
fun ChengfenPage(indexScreenVideoModel: IndexScreenVideoModel) {
    Column(Modifier.padding(16.dp)) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = indexScreenVideoModel.profileLink,
            onValueChange = { indexScreenVideoModel.profileLink = it },
            label = {
                Text(text = "请输入用户的主页链接")
            },
            singleLine = true
        )
        OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
            indexScreenVideoModel.chaChengFen()
        }) {
            Text(text = "立即查询成分")
        }

        when {
            indexScreenVideoModel.cfLoading -> {
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
            indexScreenVideoModel.cfError -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "加载失败，请重试")
                }
            }
            indexScreenVideoModel.sublist.isNotEmpty() -> {
                ChengfengResult(indexScreenVideoModel)
            }
        }
    }
}

val PIE_COLORS = listOf(
    Color.Red,
    Color.Blue,
    Color.Green,
    Color.Yellow,
    Color.Black,
    Color.LightGray,
    Color.Magenta
)

@Composable
private fun ChengfengResult(indexScreenVideoModel: IndexScreenVideoModel) {
    val context = LocalContext.current
    val data = indexScreenVideoModel.sublist.analyse()
    Column {
        Text(text = "${indexScreenVideoModel.name} 的成分:", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        Spacer(modifier = Modifier.height(10.dp))
        PieChart(
            PieChartData(
                entries = data.map {
                    PieChartEntry(
                        value = it.value,
                        label = buildAnnotatedString { append(it.key) }
                    )
                },
                colors = PIE_COLORS
            )
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var content = "用户 ${indexScreenVideoModel.name} 的成分如下:\n"
            data.forEach {
                content += "* ${it.key}: ${(it.value * 100).formatToString()}%\n"
            }
            content += "成分仅供参考"

            clipboardManager.setPrimaryClip(
                ClipData.newPlainText(
                    null, content
                )
            )
            Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "复制成分")
        }
    }
}