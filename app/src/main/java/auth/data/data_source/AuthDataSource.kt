package auth.data.data_source

import com.holparb.notemark.core.data.networking.REGISTER_ENDPOINT
import com.holparb.notemark.core.data.networking.constructUrl
import com.holparb.notemark.core.data.networking.safeCall
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

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
                contentType(ContentType.Application.Json)
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
}