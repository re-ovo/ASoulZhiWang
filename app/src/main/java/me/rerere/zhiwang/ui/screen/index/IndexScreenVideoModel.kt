package me.rerere.zhiwang.ui.screen.index

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.zhiwang.api.zhiwang.Request
import me.rerere.zhiwang.api.zhiwang.Response
import me.rerere.zhiwang.api.zuowen.ZuowenPageSource
import me.rerere.zhiwang.repo.ZuowenRepo
import javax.inject.Inject

@HiltViewModel
class IndexScreenVideoModel @Inject constructor(
    private val zhiwangRepo: ZuowenRepo
) : ViewModel() {
    // 查重
    var loading by mutableStateOf(false)
    var content by mutableStateOf("嘉然的脚小小的香香的，不像手经常使用来得灵活，但有一种独特的可爱的笨拙，嫩嫩的脚丫光滑细腻")
    val queryResult = MutableLiveData<Response>()
    var error by mutableStateOf(false)
    var lastQuery by mutableStateOf(0L)

    // 小作文
    val pager = Pager(
        config = PagingConfig(
            pageSize = 32,
            prefetchDistance = 1,
            initialLoadSize = 32
        )
    ) {
        ZuowenPageSource(zhiwangRepo)
    }.flow.cachedIn(viewModelScope)

    fun query() {
        lastQuery = System.currentTimeMillis()
        viewModelScope.launch {
            loading = true
            error = false

            // 查重
            val result = zhiwangRepo.query(Request(content))
            queryResult.value = result

            loading = false
            if (queryResult.value == null) error = true
        }
    }

    fun resetResult() {
        queryResult.value = null
    }
}