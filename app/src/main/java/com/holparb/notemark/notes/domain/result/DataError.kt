package com.holparb.notemark.notes.domain.result

import com.holparb.notemark.core.domain.result.Error
import com.holparb.notemark.core.domain.result.NetworkError

interface DataError: Error {
    data class RemoteError(val error: NetworkError): DataError
    data class LocalError(val error: DatabaseError): DataError
}