package com.holparb.notemark.auth.data.repository

import com.holparb.notemark.auth.data.data_source.AuthDataSource
import com.holparb.notemark.auth.domain.repository.AuthRepository
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.core.domain.session_storage.SessionData
import com.holparb.notemark.core.domain.session_storage.SessionStorage
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import java.util.UUID

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val userPreferences: UserPreferences,
    private val sessionStorage: SessionStorage
): AuthRepository {

    override suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<Unit, NetworkError> {
        val result = authDataSource.registerUser(
            email = email,
            username = username,
            password = password
        )
        return when(result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> Result.Success(Unit)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<String, NetworkError> {
        val result = authDataSource.login(
            email = email,
            password = password
        )
        return when(result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> {
                userPreferences.saveUsername(result.data.username!!)
                val userId = UUID.nameUUIDFromBytes(email.toByteArray()).toString()
                userPreferences.saveUserId(userId)
                sessionStorage.updateSessionData(
                    SessionData(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken
                    )
                )
                Result.Success(result.data.username)
            }
        }
    }
}