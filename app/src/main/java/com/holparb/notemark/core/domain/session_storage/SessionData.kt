package com.holparb.notemark.core.domain.session_storage

import kotlinx.serialization.Serializable

@Serializable
data class SessionData(
    val accessToken: String? = null,
    val refreshToken: String? = null
)
