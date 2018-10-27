package com.pntstudio.buzz.tedaudio.helps

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

class Config(val context: Context)  {
    protected val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var isShuffleEnabled: Boolean
        get() = prefs.getBoolean(SHUFFLE, false)

        set(shuffle) = prefs!!.edit().putBoolean(SHUFFLE, shuffle).apply()

    var equalizer: Int
        get() = prefs.getInt(EQUALIZER, 0)
        set(equalizer) = prefs.edit().putInt(EQUALIZER, equalizer).apply()

    var currentPlaylist: Int
        get() = prefs.getInt(CURRENT_PLAYLIST, ALL_SONGS_PLAYLIST_ID)
        set(currentPlaylist) = prefs.edit().putInt(CURRENT_PLAYLIST, currentPlaylist).apply()

    var repeatSong: Boolean
        get() = prefs.getBoolean(REPEAT_SONG, false)
        set(repeat) = prefs.edit().putBoolean(REPEAT_SONG, repeat).apply()

    var autoplay: Boolean
        get() = prefs.getBoolean(AUTOPLAY, true)
        set(autoplay) = prefs.edit().putBoolean(AUTOPLAY, autoplay).apply()

    var alarm_turn_off: Boolean
        get() = prefs.getBoolean(ALARM_TURN_OFF, false)
        set(alarm_turn_off) = prefs.edit().putBoolean(ALARM_TURN_OFF, alarm_turn_off).apply()

    fun getOriginLink(key:String):String {
        return prefs.getString(key,"")
    }

    fun setOriginLink(key:String,value:String){
        return prefs.edit().putString(key,value).apply()
    }
    fun isMyServiceRunning(serviceClass: Class<*>,activity:Activity): Boolean {
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun getNumberClick():Int {
        return prefs.getInt("key_click",0)
    }

    fun setNumberClick(value:Int){
        return prefs.edit().putInt("key_click",value).apply()
    }









}
