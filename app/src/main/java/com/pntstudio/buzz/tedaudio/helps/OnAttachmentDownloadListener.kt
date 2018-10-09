package com.pntstudio.buzz.tedaudio.helps

interface OnAttachmentDownloadListener {
    fun onAttachmentDownloadedSuccess()
    fun onAttachmentDownloadedError()
    fun onAttachmentDownloadedFinished()
    fun onAttachmentDownloadUpdate(percent: Int)
}
