package com.pntstudio.buzz.tedaudio.helps

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


class ProgressResponseBody(private val responseBody: ResponseBody, private val progressListener: OnAttachmentDownloadListener?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)

                totalBytesRead += if (bytesRead.compareTo(-1) !=0) bytesRead else 0

                val percent = if (bytesRead.compareTo(-1)==0) 100f else totalBytesRead.toFloat() / responseBody.contentLength().toFloat() * 100

                if (progressListener != null)
                    progressListener!!.onAttachmentDownloadUpdate(percent.toInt())

                return bytesRead
            }
        }
    }
}