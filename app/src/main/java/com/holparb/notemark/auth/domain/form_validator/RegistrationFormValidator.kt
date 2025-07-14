package com.holparb.notemark.auth.domain.form_validator

interface RegistrationFormValidator: LoginFormValidator {
    fun validateUsername(username: String): FormValidationResult
    fun validateRepeatPassword(password: String, repeatPassword: String): FormValidationResult
}