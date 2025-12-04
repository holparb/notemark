package com.holparb.notemark.core.datasync.domain

interface DataSync {
    fun enqueueNoteSync(repeatInterval: Long)
}