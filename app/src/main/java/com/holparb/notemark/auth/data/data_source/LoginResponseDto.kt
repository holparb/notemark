package com.holparb.notemark.auth.data.data_source

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val username: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
