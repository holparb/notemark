package com.holparb.notemark.auth.data.data_source

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationBodyDto (
    val username: String,
    val email: String,
    val password: String
)
