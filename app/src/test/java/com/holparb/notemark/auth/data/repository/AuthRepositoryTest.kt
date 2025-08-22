package com.holparb.notemark.auth.data.repository

import com.holparb.notemark.auth.data.data_source.AuthDataSource
import com.holparb.notemark.auth.data.data_source.LoginResponseDto
import com.holparb.notemark.auth.domain.repository.AuthRepository
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.core.domain.session_storage.SessionData
import com.holparb.notemark.core.domain.session_storage.SessionStorage
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRepositoryTest {
    private lateinit var authDataSource: AuthDataSource
    private lateinit var userPreferences: UserPreferences
    private lateinit var sessionStorage: SessionStorage
    private lateinit var authRepository: AuthRepository

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        authDataSource = mockk()
        userPreferences = mockk<UserPreferences>()
        sessionStorage = mockk<SessionStorage>()
        authRepository = AuthRepositoryImpl(authDataSource, userPreferences, sessionStorage)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun user_registration_is_successful() = runBlocking {
        val username = "user"
        val password = "password"
        val email = "email@example.com"
        coEvery {
            authDataSource.registerUser(
                username = username,
                email = email,
                password = password
            )
        } returns Result.Success(Unit)
        val result = authRepository.registerUser(username, email, password)
        assertTrue(result is Result.Success)
    }

    @Test
    fun network_error_during_registration() = runBlocking {
        NetworkError.entries.forEach { error ->
            coEvery {
                authDataSource.registerUser(any<String>(), any<String>(), any<String>())
            } returns Result.Error(error)

            val result = authRepository.registerUser("username", "email", "password")
            assertTrue(result is Result.Error)
            assertEquals(error, (result as Result.Error).error)
        }
    }

    @Test
    fun login_is_successful() = runBlocking {
        val loginResponseDto = LoginResponseDto(
            username = "User",
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        )
        coEvery { userPreferences.saveUsername(any<String>()) } returns Unit
        coEvery { sessionStorage.updateSessionData(any<SessionData>()) } returns Unit
        coEvery { authDataSource.login(any<String>(), any<String>()) } returns Result.Success(loginResponseDto)

        val result = authRepository.login("email", "password")

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { userPreferences.saveUsername(loginResponseDto.username!!) }
        coVerify(exactly = 1) { sessionStorage.updateSessionData(
            SessionData(
                accessToken = loginResponseDto.accessToken,
                refreshToken = loginResponseDto.refreshToken
            )
        ) }
        assertEquals(loginResponseDto.username, (result as Result.Success).data)
    }

    @Test
    fun network_error_during_login() = runBlocking {
        coEvery { userPreferences.saveUsername(any<String>()) } returns Unit
        coEvery { sessionStorage.updateSessionData(any<SessionData>()) } returns Unit
        NetworkError.entries.forEach { error ->
            coEvery { authDataSource.login(any<String>(), any<String>()) } returns Result.Error(error)
            val result = authRepository.login("email", "password")

            coVerify(exactly = 0) { userPreferences.saveUsername(any<String>()) }
            assertTrue(result is Result.Error)
            assertEquals(error, (result as Result.Error).error)
        }
    }
}