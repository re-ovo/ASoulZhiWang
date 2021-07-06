package me.rerere.zhiwang.api.bilibili

import com.google.gson.Gson
import me.rerere.zhiwang.util.await
import okhttp3.OkHttpClient
import okhttp3.Request

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
}