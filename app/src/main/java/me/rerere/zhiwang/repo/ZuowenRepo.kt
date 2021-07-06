package me.rerere.zhiwang.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.zhiwang.api.zhiwang.Request
import me.rerere.zhiwang.api.zhiwang.ZhiWangService
import me.rerere.zhiwang.api.zuowen.ZuowenService
import me.rerere.zhiwang.util.autoRetry

class ZuowenRepo(
    private val zhiWangService: ZhiWangService,
    private val zuowenService: ZuowenService
) {
    suspend fun query(content: Request) = withContext(Dispatchers.IO) {
        autoRetry {
            zhiWangService.query(content)
        }?.apply {
            this.data.related = this.data.related.sortedBy {
                ((it[1] as Map<*,*>)["ctime"] as Double).toLong()
            }
        }
    }

    suspend fun getZuowen(pageNum: Int, pageSize: Int) = withContext(Dispatchers.IO) {
        try {
            zuowenService.getZuowenList(pageNum, pageSize)
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}