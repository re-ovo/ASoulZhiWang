package me.rerere.zhiwang.ui.screen.index.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rerere.zhiwang.R

@Composable
fun AboutPage() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Card {
            Column(Modifier.padding(16.dp)) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    painter = painterResource(R.drawable.asoul),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                Text(text = "ASoul简介", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "A-SOUL是乐华娱乐年度最新企划中打造的虚拟偶像女团，成员由向晚（Ava）、贝拉（Bella）、珈乐（Carol）、嘉然（Diana）、乃琳（Eileen）五人组成，于2020年11月以“乐华娱乐首个虚拟偶像团体”名义出道。")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Card {
            Column(Modifier.padding(16.dp)) {
                Text(text = "本APP简介", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "项目地址: https://github.com/jiangdashao/asoulzhiwang")
                Text(text = "非常感谢:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "查重API: https://asoulcnki.asia/")
                Text(text = "小作文库: https://asoul.icu/")
            }
        }
    }
}