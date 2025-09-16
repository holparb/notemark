package com.holparb.notemark.core.presentation.util

import android.content.Context
import com.holparb.notemark.R
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.notes.domain.result.DatabaseError

fun NetworkError.toString(context: Context): String {
    val resId = when(this) {
        NetworkError.BAD_REQUEST -> R.string.something_went_wrong
        NetworkError.UNAUTHORIZED -> R.string.unauthorized
        NetworkError.METHOD_NOT_ALLOWED -> R.string.something_went_wrong
        NetworkError.CONFLICT -> R.string.conflict
        NetworkError.TOO_MANY_REQUEST -> R.string.too_many_requests
        NetworkError.CONNECTION_FAILED -> R.string.could_not_connect
        NetworkError.SERIALIZATION -> R.string.something_went_wrong
        NetworkError.UNKNOWN -> R.string.unknown_error_occurred
    }
    return context.getString(resId)
}

fun DatabaseError.toString(context: Context): String {
    val resId = when(this) {
        DatabaseError.UPSERT_FAILED -> R.string.upsert_failed
        DatabaseError.FETCH_FAILED -> R.string.fetch_failed
        DatabaseError.DELETE_FAILED -> R.string.database_delete_failed
    }
    return context.getString(resId)
}