package me.rerere.zhiwang.ui.screen.zuowen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.rerere.zhiwang.repo.ZuowenRepo
import me.rerere.zhiwang.util.HtmlFormatUtil
import org.jsoup.Jsoup
import javax.inject.Inject


@HiltViewModel
class ZuowenViewModel @Inject constructor(
    private val zuowenRepo: ZuowenRepo
) : ViewModel() {
    var loading by mutableStateOf(false)
    var error by mutableStateOf(false)

    // article info
    var id by mutableStateOf("")
    var title by mutableStateOf("")
    var author by mutableStateOf("")
    var content by mutableStateOf("")

    // 加载小作文详细内容
    fun load(id: String, title: String, author: String) {

        this.id = id
        this.title = title
        this.author = author

        viewModelScope.launch {
            loading = true
            error = false

            delay(500) // 等一下动画效果

            val content = zuowenRepo.getZuowenContent(id)
            content?.let {
                // 利用JSOUP移除HTML元素
                this@ZuowenViewModel.content =
                    HtmlFormatUtil.getPlainText(Jsoup.parse(it.htmlContent).root())
                        ?.replace(regex = Regex("[\\r\\n]+"), replacement = "\n")
                        ?.trim()
                        ?: "解析错误"
            } ?: kotlin.run {
                // 加载错误
                error = true
                println("加载小作文内容失败")
            }

            loading = false
        }
    }
}