package com.holparb.notemark.auth.domain.repository

import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result

interface AuthRepository {
    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<Unit, NetworkError>

    suspend fun login(
        email: String,
        password: String
    ): Result<String, NetworkError>
}