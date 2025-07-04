package auth.domain.register

class RegistrationFormValidatorImpl: RegistrationFormValidator {

    override fun validateUsername(username: String): Boolean {
        return username.length in 3 .. 20
    }

    override fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9+_.-]+@[a-z-]+\\.[a-z]+".toRegex()
        return email.isNotBlank()
                && email.matches(emailPattern)
    }

    override fun validatePassword(password: String): Boolean {
        val hasValidLength = password.length >= 8
        val hasDigitOrSpecialChar = password.all { !it.isLetter() }
        return hasValidLength && hasDigitOrSpecialChar
    }
}