package com.holparb.notemark.auth.domain.form_validator

import android.content.Context
import com.holparb.notemark.R

class RegistrationFormValidatorImpl(
    private val context: Context
): RegistrationFormValidator {

    override fun validateUsername(username: String): FormValidationResult {
        return when {
            username.length < 3 -> FormValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.username_short_than_3_characters)
            )
            username.length > 20 -> FormValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.username_longer_than_20_characters)
            )
            else -> FormValidationResult(isValid = true)
        }
    }

    override fun validateEmail(email: String): FormValidationResult {
        val emailPattern = "[a-zA-Z0-9+_.-]+@[a-z-]+\\.[a-z]+".toRegex()
        return when {
            email.isBlank() || !email.matches(emailPattern) -> FormValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.invalid_email_provided)
            )
            else -> FormValidationResult(isValid = true)
        }
    }

    override fun validatePassword(password: String): FormValidationResult {
        val hasValidLength = password.length >= 8
        val hasDigitOrSpecialChar = password.any { !it.isLetter() }
        return when {
            !hasValidLength || !hasDigitOrSpecialChar -> FormValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.password_validation_error)
            )
            else -> FormValidationResult(isValid = true)
        }
    }

    override fun validateRepeatPassword(
        password: String,
        repeatPassword: String
    ): FormValidationResult {
        return when {
            password != repeatPassword -> FormValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.passwords_do_not_match)
            )
            else -> FormValidationResult(isValid = true)
        }
    }


}