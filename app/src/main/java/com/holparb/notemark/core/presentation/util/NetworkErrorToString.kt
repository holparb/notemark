package com.holparb.notemark.core.presentation.util

import android.content.Context
import com.holparb.notemark.R
import com.holparb.notemark.core.domain.result.NetworkError

fun NetworkError.toString(context: Context): String {
    val resId = when(this) {
        NetworkError.BAD_REQUEST -> R.string.something_went_wrong
        NetworkError.UNAUTHORIZED -> R.string.unauthorized
        NetworkError.METHOD_NOT_ALLOWED -> R.string.something_went_wrong
        NetworkError.CONFLICT -> R.string.conflict
        NetworkError.TOO_MANY_REQUEST -> R.string.too_many_requests
        NetworkError.CONNECTION_FAILED -> R.string.could_not_connect
        NetworkError.SERIALIZATION -> R.string.something_went_wrong
        NetworkError.UNKNOWN -> R.string.unkown_error
    }
    return context.getString(resId)
}