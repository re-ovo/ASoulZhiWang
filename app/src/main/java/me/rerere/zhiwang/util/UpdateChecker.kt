package me.rerere.zhiwang.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


private const val TAG = "UpdateChecker"

suspend fun checkUpdate(context: Context): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("https://raw.githubusercontent.com/jiangdashao/ASoulZhiWang/master/app/build.gradle")
                .get()
                .build()
            val response = okHttpClient.newCall(request).await()
            require(response.isSuccessful)
            val content = response.body!!.string()
            require(content.isNotEmpty())
            val latestVersion = content.let {
                it.substring(
                    it.indexOf("versionCode") + 12,
                    it.indexOf(char = '\n', startIndex = it.indexOf("versionCode"))
                )
            }
            val latestVersionCode = latestVersion.toInt()
            val currentVersionCode = getAppVersionCode(context)
            Log.i(TAG, "checkUpdate: Latest: $latestVersionCode")
            if (latestVersionCode > currentVersionCode) {
                Log.i(
                    TAG,
                    "checkUpdate: Found a update! (current: $currentVersionCode, latest: $latestVersionCode)"
                )
            } else {
                Log.i(TAG, "checkUpdate: There is no update")
            }
            latestVersionCode > currentVersionCode
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

fun getAppVersionCode(context: Context): Long {
    var appVersionCode: Long = 0
    try {
        val packageInfo: PackageInfo = context.applicationContext
            .packageManager
            .getPackageInfo(context.packageName, 0)
        appVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return appVersionCode
}