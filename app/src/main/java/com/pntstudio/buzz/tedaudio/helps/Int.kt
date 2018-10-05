package com.pntstudio.buzz.tedaudio.helps

import android.annotation.SuppressLint
import android.graphics.Color
import java.util.*

/**
 * Created by admin on 10/5/18.
 */
fun Int.getFormattedDuration(): String {
    val sb = StringBuilder(8)
    val hours = this / 3600
    val minutes = this % 3600 / 60
    val seconds = this % 60

    if (this >= 3600) {
        sb.append(String.format(Locale.getDefault(), "%02d", hours)).append(":")
    }

    sb.append(String.format(Locale.getDefault(), "%02d", minutes))
    sb.append(":").append(String.format(Locale.getDefault(), "%02d", seconds))
    return sb.toString()
}

@SuppressLint("Range")
fun Int.getContrastColor(): Int {
    val DARK_GREY = -13421773
    val y = (299 * Color.red(this) + 587 * Color.green(this) + 114 * Color.blue(this)) / 1000
    return if (y >= 149) DARK_GREY else Color.WHITE
}

