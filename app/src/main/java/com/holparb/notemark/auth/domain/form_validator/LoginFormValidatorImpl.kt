package com.holparb.notemark.auth.domain.form_validator

import android.content.Context
import com.holparb.notemark.R

class LoginFormValidatorImpl(
    private val context: Context
): LoginFormValidator {
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
        return if(password.isBlank()) {
            FormValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.password_cannot_be_empty)
            )
        } else {
            FormValidationResult(isValid = true)
        }
    }
}