package me.rerere.zhiwang.util

import android.util.Log
import androidx.annotation.IntRange
import me.rerere.zhiwang.api.zhiwang.Response

private const val TAG = "AutoRetry"

/**
 * 自动重试函数
 *
 * @param maxRetry 重试次数
 * @param action 重试体
 * @return 最终响应
 */
suspend fun autoRetry(
    @IntRange(from = 2) maxRetry: Int = 10, // 重连次数
    action: suspend () -> Response
): Response? {
    repeat(maxRetry - 1) {
        Log.i(TAG, "autoRetry: Try to get response: ${it + 1}/$maxRetry")
        val start = System.currentTimeMillis()
        val response = try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        if (response != null) {
            Log.i(
                TAG,
                "autoRetry: Successful get response (${System.currentTimeMillis() - start} ms)"
            )
            return response
        }
    }
    Log.i(TAG, "autoRetry: Try to get response: $maxRetry*/$maxRetry")
    return try { action() } catch (e: Exception){
        e.printStackTrace()
        null
    }
}