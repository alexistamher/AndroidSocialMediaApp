package dev.spooky.socialmediaapp.data.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun <T : HttpClientEngineConfig> httpClient(engine: HttpClientEngineFactory<T>): HttpClient =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                json =
                    Json {
                        ignoreUnknownKeys = true
                    },
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

fun httpClient(engine: HttpClientEngine): HttpClient =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                json =
                    Json {
                        ignoreUnknownKeys = true
                    },
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
