package me.rerere.zhiwang.util.android

import android.content.ClipboardManager
import android.content.Context

fun Context.getClipboardContent(): String? {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.let {
        if (it.hasPrimaryClip() && it.primaryClip!!.itemCount > 0) {
            val text = it.primaryClip!!.getItemAt(0).text
            if (text.length >= 10) {
                return text.toString()
            }
        }
    }
    return null
}