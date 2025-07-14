package com.holparb.notemark.auth.domain.form_validator

interface LoginFormValidator {
    fun validateEmail(email: String): FormValidationResult
    fun validatePassword(password: String): FormValidationResult
}