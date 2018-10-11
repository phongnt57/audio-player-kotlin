package com.pntstudio.buzz.tedaudio.model

import java.io.Serializable

/**
 * Created by admin on 10/8/18.
 */
data  class Download(
        var title:String,
        var link: String,
        var status: String,
        var progess: Int,
        var fileSize: Int

):Serializable