package me.rerere.zhiwang.api.bilibili

import com.google.gson.Gson
import kotlinx.coroutines.delay
import me.rerere.zhiwang.util.await
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class BilibiliUtil(
    private val okHttpClient: OkHttpClient
) {
    private val gson = Gson()

    suspend fun getUserInfo(mId: Int): BilibiliUserData? = try {
        val request = Request.Builder()
            .url("https://api.bilibili.com/x/space/acc/info?mid=$mId")
            .get()
            .build()
        val response = okHttpClient.newCall(request).await()
        require(response.isSuccessful)
        val json = response.body?.string()
        // println(json)
        val user = gson.fromJson(json, BilibiliUserData::class.java)
        user
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getAllSubLis(profileLink: String): SubResult? = try {
        val profileRequest = Request.Builder()
            .url(profileLink)
            .get()
            .build()

        val profileResponse = okHttpClient.newCall(profileRequest).await()
        // 用户ID
        val id = profileResponse.request.url.toString().let {
            it.substring(
                it.indexOf("com/") + 4,
                it.indexOf('?', it.indexOf("com/") + 4)
            )
        }
        // 用户名
        val name = Jsoup.parse(profileResponse.body?.string()).title().let {
            it.substring(0 until it.indexOf("的个人空间"))
        }
        // 关注的用户
        val list = hashSetOf<SubUser>()
        repeat(5) {
            val subRequest = Request.Builder()
                .url("https://api.bilibili.com/x/relation/followings?vmid=$id&pn=${it + 1}&ps=50&order=desc")
                .get()
                .build()
            val subList = try {
                val subResponse = okHttpClient.newCall(subRequest).await()
                gson.fromJson(subResponse.body!!.string(), SubList::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            subList?.data?.list?.forEach { user ->
                list.add(
                    SubUser(mid = user.mid, name = user.uname)
                )
            }
            delay(50)
        }
        repeat(5) {
            val subRequest = Request.Builder()
                .url("https://api.bilibili.com/x/relation/followings?vmid=$id&pn=${it + 1}&ps=50&order=asc")
                .get()
                .build()
            val subList = try {
                val subResponse = okHttpClient.newCall(subRequest).await()
                gson.fromJson(subResponse.body!!.string(), SubList::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            subList?.data?.list?.forEach { user ->
                list.add(
                    SubUser(mid = user.mid, name = user.uname)
                )
            }
            delay(50)
        }
        list.forEach {
            println("${it.mid} - ${it.name}")
        }
        val result = SubResult(
            id = id,
            name = name,
            subList = list
        )
        result
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}