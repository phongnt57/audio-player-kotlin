package com.pntstudio.buzz.tedaudio.receiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pntstudio.buzz.tedaudio.helps.*


class ControlActionsListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when (action) {
            PREVIOUS, PLAYPAUSE, NEXT, FINISH -> context.sendIntent(action)
        }
    }
}

