package me.rerere.zhiwang

import android.app.Application
import android.content.Context

class AppContext : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}