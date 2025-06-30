package com.holparb.notemark.core.domain.result

enum class NetworkError: Error {
    BAD_REQUEST,
    UNAUTHORIZED,
    METHOD_NOT_ALLOWED,
    CONFLICT,
    TOO_MANY_REQUEST,
    NO_INTERNET_CONNECTION,
    SERIALIZATION,
    UNKNOWN
}