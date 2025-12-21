package com.holparb.notemark.core.data.networking

import com.holparb.notemark.BuildConfig
import com.holparb.notemark.core.data.session_storage.RefreshTokenRequest
import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.core.domain.session_storage.SessionData
import com.holparb.notemark.core.domain.session_storage.SessionStorage
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val sessionStorage: SessionStorage,
    private val userPreferences: UserPreferences
) {

    fun create(
        engine: HttpClientEngine
    ): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header(
                    USER_EMAIL_HEADER,
                    BuildConfig.USER_EMAIL
                )
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val sessionData = sessionStorage.getSessionData()
                        BearerTokens(
                            accessToken = sessionData.accessToken ?: "",
                            refreshToken = sessionData.refreshToken ?: ""
                        )
                    }
                    refreshTokens {
                        val sessionData = sessionStorage.getSessionData()
                        val result = safeCall<SessionData> {
                            client.post(
                                urlString = constructUrl(REFRESH_TOKEN_ENDPOINT)
                            ) {
                                setBody(
                                    RefreshTokenRequest(
                                        refreshToken = sessionData.refreshToken
                                    )
                                )
                                markAsRefreshTokenRequest()
                            }
                        }
                        when(result) {
                            is Result.Error -> {
                                // TODO call logout endpoint
                                sessionStorage.updateSessionData(
                                    SessionData()
                                )
                                userPreferences.saveUserId("")
                                userPreferences.saveUsername("")
                                BearerTokens("", "")
                            }
                            is Result.Success -> {
                                sessionStorage.updateSessionData(
                                    SessionData(
                                        accessToken = result.data.accessToken,
                                        refreshToken = result.data.refreshToken
                                    )
                                )
                                BearerTokens(
                                    accessToken = result.data.accessToken!!,
                                    refreshToken = result.data.refreshToken
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}