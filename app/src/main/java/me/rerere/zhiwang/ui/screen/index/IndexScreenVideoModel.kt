package me.rerere.zhiwang.ui.screen.index

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.rerere.zhiwang.api.zhiwang.Request
import me.rerere.zhiwang.api.zhiwang.Response
import me.rerere.zhiwang.repo.ZhiwangRepo
import javax.inject.Inject

@HiltViewModel
class IndexScreenVideoModel @Inject constructor(
    private val zhiwangRepo: ZhiwangRepo
) : ViewModel() {
    var loading by mutableStateOf(false)
    var content by mutableStateOf("嘉然的脚小小的香香的，不像手经常使用来得灵活，但有一种独特的可爱的笨拙，嫩嫩的脚丫光滑细腻")
    val queryResult = MutableLiveData<Response>()
    var error by mutableStateOf(false)

    fun query() {
        viewModelScope.launch {
            loading = true
            error = false
            delay(1000)
            withContext(Dispatchers.IO){
                val result = zhiwangRepo.query(Request(content))
                withContext(Dispatchers.Main){
                    queryResult.value = result
                }
            }
            loading = false
            if(queryResult.value == null) error = true
        }
    }

    fun resetResult() {
        queryResult.value = null
    }
}