package com.holparb.notemark.auth.domain.token

data class Token(
    val accessToken: String,
    val refreshToken: String
)
