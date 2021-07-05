package me.rerere.zhiwang.ui.screen.index

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.rerere.zhiwang.ui.public.FullScreenTopBar

@Composable
fun IndexScreen() {
    Scaffold(
        topBar = {
            TopBar()
        }
    ) {
        Content()
    }
}

@Composable
private fun Content() {
    var content by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            value = content,
            onValueChange = { content = it },
            placeholder = {
                Text(text = "输入要查重的小作文")
            },
            colors = textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(5.dp)
        )

        Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), onClick = {
            // TODO
        }) {
            Text(text = "立即查重捏")
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