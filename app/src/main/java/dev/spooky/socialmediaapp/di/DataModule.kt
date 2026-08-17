package dev.spooky.socialmediaapp.di

import dev.spooky.socialmediaapp.BuildConfig
import dev.spooky.socialmediaapp.data.repository.AuthRepositoryImpl
import dev.spooky.socialmediaapp.data.util.httpClient
import dev.spooky.socialmediaapp.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single { AuthRepositoryImpl(get(), get(named("API_URL")), get()) } bind AuthRepository::class
    factory<HttpClient> { httpClient(CIO) }
    single(named("API_URL"), true) {
        "${BuildConfig.BASE_API_URL}/api/v1"
    }
}