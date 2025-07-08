package auth.di

import auth.data.data_source.AuthDataSource
import auth.data.repository.AuthRepositoryImpl
import auth.domain.form_validator.LoginFormValidator
import auth.domain.form_validator.LoginFormValidatorImpl
import auth.domain.form_validator.RegistrationFormValidator
import auth.domain.form_validator.RegistrationFormValidatorImpl
import auth.domain.repository.AuthRepository
import auth.presentation.login.LoginViewModel
import auth.presentation.register.RegisterViewModel
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