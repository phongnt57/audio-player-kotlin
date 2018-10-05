package com.pntstudio.buzz.tedaudio.helps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.pntstudio.buzz.tedaudio.services.MusicService


fun Context.sendIntent(action: String) {
    Intent(this, MusicService::class.java).apply {
        this.action = action
        try {
            startService(this)
        } catch (ignored: Exception) {
        }
    }
}

fun Context.getSharedPrefs(): SharedPreferences? {
    return getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
}
val Context.config: Config get() = Config.newInstance(applicationContext)

