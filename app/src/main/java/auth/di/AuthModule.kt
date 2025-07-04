package auth.di

import auth.domain.form_validator.RegistrationFormValidator
import auth.domain.form_validator.RegistrationFormValidatorImpl
import auth.presentation.login.LoginViewModel
import auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::RegistrationFormValidatorImpl) bind RegistrationFormValidator::class
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}