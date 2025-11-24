package com.holparb.notemark.app.domain

interface DataSync {
    fun enqueueNoteSync(repeatInterval: Long)
}