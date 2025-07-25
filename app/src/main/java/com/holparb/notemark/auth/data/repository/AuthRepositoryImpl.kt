package com.holparb.notemark.auth.data.repository

import com.holparb.notemark.auth.data.data_source.AuthDataSource
import com.holparb.notemark.auth.data.data_source.toToken
import com.holparb.notemark.auth.domain.repository.AuthRepository
import com.holparb.notemark.auth.domain.token.Token
import com.holparb.notemark.core.data.user_preferences.UserPreferences
import com.holparb.notemark.core.data.user_preferences.UserPreferencesDataStore
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val userPreferencesDataStore: UserPreferencesDataStore
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
                userPreferencesDataStore.updateUserData(
                    UserPreferences(
                        username = result.data.username,
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken
                    )
                )
                Result.Success(result.data.username!!)
            }
        }
    }

    override suspend fun refreshToken(): Result<Token, NetworkError> {
        val result = authDataSource.refreshToken(userPreferencesDataStore.getTokens().refreshToken)

        return when(result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> {
                val tokenResponse = result.data
                if(tokenResponse.accessToken == null || tokenResponse.refreshToken == null) {
                    Result.Error(NetworkError.SERIALIZATION)
                }
                Result.Success(result.data.toToken())
            }
        }
    }

    override suspend fun getUsername(): String {
        return userPreferencesDataStore.getUsername()
    }

    override suspend fun getTokens(): Token {
        return userPreferencesDataStore.getTokens()
    }


}