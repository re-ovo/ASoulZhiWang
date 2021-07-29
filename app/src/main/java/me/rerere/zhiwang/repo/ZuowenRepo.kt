package me.rerere.zhiwang.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.zhiwang.api.bilibili.BilibiliUtil
import me.rerere.zhiwang.api.bilibili.SubResult
import me.rerere.zhiwang.api.zhiwang.Request
import me.rerere.zhiwang.api.zhiwang.ZhiWangService
import me.rerere.zhiwang.api.zuowen.ZuowenService
import me.rerere.zhiwang.util.net.autoRetry

class ZuowenRepo(
    private val zhiWangService: ZhiWangService,
    private val zuowenService: ZuowenService,
    private val bilibiliUtil: BilibiliUtil
) {
    suspend fun query(content: Request) = withContext(Dispatchers.IO) {
        autoRetry {
            zhiWangService.query(content)
        }?.apply {
            println(this)

            // 获取用户信息
            this.data.related.forEach {
                try {
                    val mid = it.reply.mid
                    val bilibiliUser = bilibiliUtil.getUserInfo(mid)
                    bilibiliUser?.let { user ->
                        it.reply.avatar = user.data.face.replace("http:", "https:")
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

    suspend fun getZuowenContent(id: String) = withContext(Dispatchers.IO){
        try {
            zuowenService.getZuowenContent(id)
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllSubLis(link: String) : SubResult? = withContext(Dispatchers.IO){
        bilibiliUtil.getAllSubLis(link)
    }
}