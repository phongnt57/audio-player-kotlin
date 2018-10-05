package com.pntstudio.buzz.tedaudio.helps

import android.content.Context
import android.text.format.DateFormat

import java.util.*

open class BaseConfig(val context: Context) {
    protected val prefs = context.getSharedPrefs()

    companion object {
        fun newInstance(context: Context) = BaseConfig(context)
    }

}

