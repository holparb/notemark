package com.holparb.notemark.auth.data.data_source

import com.holparb.notemark.auth.domain.token.Token
import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

fun TokenDto.toToken(): Token {
    return Token(
        accessToken = accessToken ?: "",
        refreshToken = refreshToken ?: ""
    )
}