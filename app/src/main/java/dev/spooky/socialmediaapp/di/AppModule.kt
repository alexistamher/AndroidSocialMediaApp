package dev.spooky.socialmediaapp.di

import dev.spooky.socialmediaapp.domain.repository.AuthRepository
import dev.spooky.socialmediaapp.domain.usecase.auth.LoginUseCase
import dev.spooky.socialmediaapp.presentation.screens.LoginViewModel
import dev.spooky.socialmediaapp.repository.AuthRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    factory<LoginUseCase> { LoginUseCase(get()) }
    viewModelOf(::LoginViewModel)
}