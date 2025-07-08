package auth.domain.form_validator

interface RegistrationFormValidator {
    fun validateUsername(username: String): FormValidationResult
    fun validateEmail(email: String): FormValidationResult
    fun validatePassword(password: String): FormValidationResult
    fun validateRepeatPassword(password: String, repeatPassword: String): FormValidationResult
}