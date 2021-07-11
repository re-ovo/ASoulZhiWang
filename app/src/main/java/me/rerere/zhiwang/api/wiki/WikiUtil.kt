package me.rerere.zhiwang.api.wiki

import com.google.gson.Gson
import me.rerere.zhiwang.util.net.await
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class WikiUtil @Inject constructor(
    private val okHttpClient: OkHttpClient
){
    private val gson = Gson()

    suspend fun loadWiki() : List<WikiList.WikiListItem>? {
        return try {
            val request = Request.Builder()
                .url("https://gitee.com/RE_OVO/asoulzhiwang/raw/master/wiki.json")
                .get()
                .build()
            val response = okHttpClient.newCall(request).await()
            val body = response.body?.string()
            val wikiList = gson.fromJson(body, WikiList::class.java)
            wikiList
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}