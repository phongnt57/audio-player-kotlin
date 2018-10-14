package com.pntstudio.buzz.tedaudio.model

import java.util.ArrayList


import java.util.*

class Events {
    class SongChanged internal constructor(val song: MediaItemData?)

    class SongStateChanged internal constructor(val isPlaying: Boolean)

    class PlaylistUpdated internal constructor(val songs: ArrayList<MediaItemData>)

    class ProgressUpdated internal constructor(val progress: Int)

    class DurationUpdate internal constructor(val duration: Int)

    // trigger service to resend info song
    class EmptyObject internal constructor()

    class DownloadSucess internal constructor(val download: Download)
}
