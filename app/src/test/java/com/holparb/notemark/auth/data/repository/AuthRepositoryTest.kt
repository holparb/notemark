package com.holparb.notemark.auth.data.repository

import com.holparb.notemark.auth.data.data_source.AuthDataSource
import com.holparb.notemark.auth.domain.repository.AuthRepository
import com.holparb.notemark.core.data.user_preferences.UserPreferences
import com.holparb.notemark.core.data.user_preferences.UserPreferencesDataStore
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result
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
    private lateinit var userPreferencesRepository: UserPreferencesDataStore
    private lateinit var authRepository: AuthRepository

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        authDataSource = mockk()
        userPreferencesRepository = mockk<UserPreferencesDataStore>()
        authRepository = AuthRepositoryImpl(authDataSource, userPreferencesRepository)
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
        val userPreferences = UserPreferences(username = "user", accessToken = "token", refreshToken = "token")
        coEvery { userPreferencesRepository.updateUserData(userPreferences) } returns Unit
        coEvery { authDataSource.login(any<String>(), any<String>()) } returns Result.Success(userPreferences)

        val result = authRepository.login("email", "password")

        coVerify(exactly = 1) { userPreferencesRepository.updateUserData(userPreferences) }
        assertTrue(result is Result.Success)
        assertEquals(userPreferences.username, (result as Result.Success).data)
    }

    @Test
    fun network_error_during_login() = runBlocking {
        NetworkError.entries.forEach { error ->
            coEvery { authDataSource.login(any<String>(), any<String>()) } returns Result.Error(error)
            val result = authRepository.login("email", "password")

            coVerify(exactly = 0) { userPreferencesRepository.updateUserData(any()) }
            assertTrue(result is Result.Error)
            assertEquals(error, (result as Result.Error).error)
        }
    }
}