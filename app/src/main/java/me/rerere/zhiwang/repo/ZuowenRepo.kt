package me.rerere.zhiwang.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.zhiwang.api.bilibili.BilibiliUtil
import me.rerere.zhiwang.api.zhiwang.Request
import me.rerere.zhiwang.api.zhiwang.ZhiWangService
import me.rerere.zhiwang.api.zuowen.ZuowenService
import me.rerere.zhiwang.util.autoRetry

class ZuowenRepo(
    private val zhiWangService: ZhiWangService,
    private val zuowenService: ZuowenService,
    private val bilibiliUtil: BilibiliUtil
) {
    suspend fun query(content: Request) = withContext(Dispatchers.IO) {
        autoRetry {
            zhiWangService.query(content)
        }?.apply {
            // 重新排序
            this.data.related = this.data.related.sortedBy {
                ((it[1] as Map<*,*>)["ctime"] as Double).toLong()
            }

            // 获取用户信息
            this.data.related.forEach {
                try {
                    val userInfo = it[1] as MutableMap<Any, Any>
                    val bilibiliUser = bilibiliUtil.getUserInfo((userInfo["mid"] as Double).toInt())
                    bilibiliUser?.let { user ->
                        userInfo["avatar"] = user.data.face.replace("http:", "https:")
                        println("头像: ${user.data.face}")
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
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