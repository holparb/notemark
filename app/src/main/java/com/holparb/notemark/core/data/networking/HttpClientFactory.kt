package com.holparb.notemark.core.data.networking

import com.holparb.notemark.BuildConfig
import com.holparb.notemark.auth.domain.repository.AuthRepository
import com.holparb.notemark.core.domain.result.Result
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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        engine: HttpClientEngine,
        authRepository: AuthRepository,
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
                        val tokens = authRepository.getTokens()
                        BearerTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                    }
                    refreshTokens {
                        val result = authRepository.refreshToken()
                        when(result) {
                            is Result.Error -> BearerTokens("", "")
                            is Result.Success -> BearerTokens(
                                accessToken = result.data.accessToken,
                                refreshToken = result.data.refreshToken
                            )
                        }
                    }
                }
            }
        }
    }
}