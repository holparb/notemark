package auth.data.repository

import auth.data.data_source.AuthDataSource
import auth.domain.repository.AuthRepository
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
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
}