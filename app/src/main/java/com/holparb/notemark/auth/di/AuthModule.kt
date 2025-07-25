package com.holparb.notemark.auth.di

import com.holparb.notemark.auth.data.data_source.AuthDataSource
import com.holparb.notemark.auth.data.repository.AuthRepositoryImpl
import com.holparb.notemark.auth.domain.form_validator.LoginFormValidator
import com.holparb.notemark.auth.domain.form_validator.LoginFormValidatorImpl
import com.holparb.notemark.auth.domain.form_validator.RegistrationFormValidator
import com.holparb.notemark.auth.domain.form_validator.RegistrationFormValidatorImpl
import com.holparb.notemark.auth.domain.repository.AuthRepository
import com.holparb.notemark.auth.presentation.login.LoginViewModel
import com.holparb.notemark.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::RegistrationFormValidatorImpl) bind RegistrationFormValidator::class
    singleOf(::LoginFormValidatorImpl) bind LoginFormValidator::class
    singleOf(::AuthDataSource)
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}