package auth.domain

interface RegistrationFormValidator {
    fun validateUsername(username: String): Boolean
    fun validateEmail(email: String): Boolean
    fun validatePassword(password: String): Boolean
}