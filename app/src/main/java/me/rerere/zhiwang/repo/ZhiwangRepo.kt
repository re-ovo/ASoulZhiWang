package me.rerere.zhiwang.repo

import me.rerere.zhiwang.api.zhiwang.Request
import me.rerere.zhiwang.api.zhiwang.Response
import me.rerere.zhiwang.api.zhiwang.ZhiWangService
import me.rerere.zhiwang.util.autoRetry
import java.lang.Exception

class ZhiwangRepo(
    private val zhiWangService: ZhiWangService
) {
    suspend fun query(content: Request) = autoRetry {
        zhiWangService.query(content)
    }?.apply {
        this.data.related = this.data.related.reversed()
    }
}