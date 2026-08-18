package dev.spooky.socialmediaapp.di

import dev.spooky.socialmediaapp.domain.usecase.auth.LoginUseCase
import dev.spooky.socialmediaapp.domain.usecase.auth.RegisterUseCase
import dev.spooky.socialmediaapp.presentation.screens.login.LoginViewModel
import dev.spooky.socialmediaapp.presentation.screens.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        factory<LoginUseCase> { LoginUseCase(get()) }
        factory<RegisterUseCase> { RegisterUseCase(get()) }
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
    }
