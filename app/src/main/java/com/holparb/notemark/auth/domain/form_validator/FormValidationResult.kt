package com.holparb.notemark.auth.domain.form_validator

data class FormValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)