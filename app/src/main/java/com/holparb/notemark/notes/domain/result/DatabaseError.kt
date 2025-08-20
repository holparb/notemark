package com.holparb.notemark.notes.domain.result

import com.holparb.notemark.core.domain.result.Error

enum class DatabaseError: Error {
    UPSERT_FAILED,
    FETCH_FAILED
}