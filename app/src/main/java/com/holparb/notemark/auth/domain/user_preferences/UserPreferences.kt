package com.holparb.notemark.auth.domain.user_preferences

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val username: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
