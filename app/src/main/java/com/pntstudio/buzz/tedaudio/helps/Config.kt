package com.pntstudio.buzz.tedaudio.helps

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





}
