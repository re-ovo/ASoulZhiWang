package me.rerere.zhiwang.ui.public

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.placeholder.material.placeholder
import me.rerere.zhiwang.ui.theme.PINK
import me.rerere.zhiwang.util.formatToString
import java.sql.Timestamp
import java.text.DateFormat
import java.util.*

@Composable
fun XiaoZuoWen(data: List<Any>) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse((data[2] as String).trim())
                )
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        val userInfo = data[1] as Map<*, *>
        Column(Modifier.padding(12.dp)) {
            // 作者信息
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                val (avatar, name, likes) = createRefs()
                val painter = rememberCoilPainter(userInfo["avatar"] as? String)
                Box(modifier = Modifier
                    .constrainAs(avatar){
                        start.linkTo(parent.start, 8.dp)
                        top.linkTo(parent.top)
                    }
                    .size(40.dp)
                    .clip(CircleShape)
                    .placeholder(painter.loadState is ImageLoadState.Loading)
                ){
                    Image(painter = painter, contentDescription = null, modifier = Modifier.fillMaxSize())
                }
                // 名字
                Text(
                    modifier = Modifier.constrainAs(name) {
                        start.linkTo(avatar.end, 8.dp)
                        centerVerticallyTo(avatar)
                    },
                    text = userInfo["m_name"] as String,
                    fontWeight = FontWeight.Bold,
                    color = PINK
                )
                // 点赞
                Row(modifier = Modifier.constrainAs(likes) {
                    end.linkTo(parent.end, 8.dp)
                    centerVerticallyTo(avatar)
                }, verticalAlignment = Alignment.CenterVertically) {
                    Icon(modifier = Modifier.size(15.dp), imageVector = Icons.Default.ThumbUp, contentDescription = null)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = (userInfo["like_num"] as Double).toInt().toString())
                }
            }
            // 作文内容
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(text = userInfo["content"] as String)
            }
            // 作文信息
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "重复度: ${((data[0] as Double) * 100).formatToString()}%")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "日期: ${getDateTime((userInfo["ctime"] as Double).toLong())}")
                }
            }
        }
    }
}

private fun getDateTime(time: Long): String? {
    return try {
        val timestamp = Timestamp(time)
        return DateFormat.getDateInstance(2, Locale.CHINA).format(Date(timestamp.time * 1000L))
    } catch (e: Exception) {
        e.toString()
    }
}