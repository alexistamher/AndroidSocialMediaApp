package dev.spooky.socialmediaapp.di

import dev.spooky.socialmediaapp.domain.usecase.auth.LoginUseCase
import dev.spooky.socialmediaapp.domain.usecase.auth.RegisterUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.AddPostUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.DeletePostUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostsUseCase
import dev.spooky.socialmediaapp.presentation.screens.home.HomeViewModel
import dev.spooky.socialmediaapp.presentation.screens.login.LoginViewModel
import dev.spooky.socialmediaapp.presentation.screens.register.RegisterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        factory<LoginUseCase> { LoginUseCase(get()) }
        factory<RegisterUseCase> { RegisterUseCase(get()) }
        factoryOf(::GetPostsUseCase)
        factoryOf(::AddPostUseCase)
        factoryOf(::DeletePostUseCase)

        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::HomeViewModel)
    }
