package dev.spooky.socialmediaapp.di

import dev.spooky.socialmediaapp.domain.usecase.auth.LoginUseCase
import dev.spooky.socialmediaapp.domain.usecase.auth.LogoutUseCase
import dev.spooky.socialmediaapp.domain.usecase.auth.RegisterUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.AddCommentUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.AddPostUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.AddReactionUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.DeletePostUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.DeleteReactionUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetCommentsByPostIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostByIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostsUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.ToggleReactionUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.UpdateReactionUseCase
import dev.spooky.socialmediaapp.presentation.screens.SplashViewModel
import dev.spooky.socialmediaapp.presentation.screens.home.HomeViewModel
import dev.spooky.socialmediaapp.presentation.screens.home.post.PostDetailViewModel
import dev.spooky.socialmediaapp.presentation.screens.login.LoginViewModel
import dev.spooky.socialmediaapp.presentation.screens.register.RegisterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        // useCases
        factoryOf(::LoginUseCase)
        factoryOf(::RegisterUseCase)
        factoryOf(::LogoutUseCase)
        factoryOf(::GetPostsUseCase)
        factoryOf(::AddPostUseCase)
        factoryOf(::DeletePostUseCase)
        factoryOf(::GetPostByIdUseCase)
        factoryOf(::GetCommentsByPostIdUseCase)
        factoryOf(::AddCommentUseCase)
        factoryOf(::AddReactionUseCase)
        factoryOf(::DeleteReactionUseCase)
        factoryOf(::UpdateReactionUseCase)
        factoryOf(::ToggleReactionUseCase)

        // viewModels
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::PostDetailViewModel)
        viewModelOf(::SplashViewModel)
    }
