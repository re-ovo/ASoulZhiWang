package me.rerere.zhiwang.ui.public

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.material.placeholder
import me.rerere.zhiwang.api.zhiwang.Response
import me.rerere.zhiwang.ui.theme.PINK
import me.rerere.zhiwang.util.android.setClipboardText
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun XiaoZuoWen(data: Response.Data.Related) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        context.setClipboardText(data.reply.content)
                        Toast
                            .makeText(context, "已复制该作文到剪贴板", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onTap = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(data.replyUrl.trim())
                        )
                        context.startActivity(intent)
                    }
                )
            },
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column(Modifier.padding(12.dp)) {
            // 作者信息
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                val (avatar, name, likes) = createRefs()
                val painter = rememberImagePainter(data.reply.avatar as? String)
                Box(modifier = Modifier
                    .constrainAs(avatar) {
                        start.linkTo(parent.start, 8.dp)
                        top.linkTo(parent.top)
                    }
                    .size(40.dp)
                    .clip(CircleShape)
                    .placeholder(painter.state is ImagePainter.State.Loading)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // 名字
                Text(
                    modifier = Modifier.constrainAs(name) {
                        start.linkTo(avatar.end, 8.dp)
                        centerVerticallyTo(avatar)
                    },
                    text = data.reply.mName,
                    fontWeight = FontWeight.Bold,
                    color = PINK
                )
                // 点赞
                Row(modifier = Modifier.constrainAs(likes) {
                    end.linkTo(parent.end, 8.dp)
                    centerVerticallyTo(avatar)
                }, verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = (data.reply.likeNum.toString()))
                }
            }
            // 作文内容
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(text = data.reply.content)
            }
            // 作文信息
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "重复度: ${(data.rate * 100f).toInt().coerceAtMost(100)}%")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "日期: ${getDateTime((data.reply.ctime.toLong() * 1000L))}")
                }
            }
        }
    }
}

private val timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA)
fun getDateTime(time: Long): String? {
    return try {
        return timeFormat.format(Date(time))
    } catch (e: Exception) {
        e.toString()
    }
}