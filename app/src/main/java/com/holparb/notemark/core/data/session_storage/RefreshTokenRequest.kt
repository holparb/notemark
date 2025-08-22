package com.holparb.notemark.core.data.session_storage

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String? = null
)
