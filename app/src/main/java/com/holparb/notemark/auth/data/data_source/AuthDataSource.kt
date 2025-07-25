package com.holparb.notemark.auth.data.data_source

import com.holparb.notemark.core.data.user_preferences.UserPreferences
import com.holparb.notemark.core.data.networking.LOGIN_ENDPOINT
import com.holparb.notemark.core.data.networking.REGISTER_ENDPOINT
import com.holparb.notemark.core.data.networking.constructUrl
import com.holparb.notemark.core.data.networking.safeCall
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthDataSource(
    private val httpClient: HttpClient
) {
    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<Unit, NetworkError> {
        return safeCall<Unit> {
            httpClient.post(
                urlString = constructUrl(REGISTER_ENDPOINT)
            ) {
                setBody(
                    RegistrationBodyDto(
                        username = username,
                        email = email,
                        password = password
                    )
                )
            }
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<UserPreferences, NetworkError> {
        return safeCall<UserPreferences> {
            httpClient.post(
                urlString = constructUrl(LOGIN_ENDPOINT)
            ) {
                setBody(
                    LoginBodyDto(
                        email = email,
                        password = password
                    )
                )
            }
        }
    }
}