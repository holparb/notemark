package auth.domain

class RegistrationFormValidatorImpl: RegistrationFormValidator {

    override fun validateUsername(username: String): Boolean {
        return username.length in 3 .. 20
    }

    override fun validateEmail(email: String): Boolean {
        return email.isNotBlank()
                && email.matches(Regex("[a-zA-Z0-9+_.-]+@[a-z-]+\\.[a-z]+"))
    }

    override fun validatePassword(password: String): Boolean {
        return password.length >= 8
                && password.matches(Regex("^(?=.*[A-Za-z])(?=.*[\\d\\W]).+$"))
    }
}