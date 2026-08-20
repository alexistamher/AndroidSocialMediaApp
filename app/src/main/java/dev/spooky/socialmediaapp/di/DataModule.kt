package dev.spooky.socialmediaapp.di

import dev.spooky.socialmediaapp.BuildConfig
import dev.spooky.socialmediaapp.data.repository.AuthRepositoryImpl
import dev.spooky.socialmediaapp.data.repository.CommentRepositoryImpl
import dev.spooky.socialmediaapp.data.repository.PostRepositoryImpl
import dev.spooky.socialmediaapp.data.repository.ReactionRepositoryImpl
import dev.spooky.socialmediaapp.data.util.httpClient
import dev.spooky.socialmediaapp.domain.repository.AuthRepository
import dev.spooky.socialmediaapp.domain.repository.CommentRepository
import dev.spooky.socialmediaapp.domain.repository.PostRepository
import dev.spooky.socialmediaapp.domain.repository.ReactionRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule =
    module {
        // repositories
        single {
            AuthRepositoryImpl(
                get(),
                get(named("API_URL")),
                get(),
            )
        } bind AuthRepository::class
        single {
            PostRepositoryImpl(
                get(),
                get(named("API_URL")),
                get(),
            )
        } bind PostRepository::class
        single {
            CommentRepositoryImpl(
                get(),
                get(named("API_URL")),
                get(),
            )
        } bind CommentRepository::class
        single {
            ReactionRepositoryImpl(
                get(),
                get(named("API_URL")),
                get(),
            )
        } bind ReactionRepository::class

        // http
        factory<HttpClient> { httpClient(CIO) }
        single(named("API_URL"), true) {
            "${BuildConfig.BASE_API_URL}/api/v1"
        }
    }
