package dev.spooky.socialmediaapp.data.repository

import dev.spooky.socialmediaapp.data.dto.AuthRequest
import dev.spooky.socialmediaapp.data.dto.AuthResponse
import dev.spooky.socialmediaapp.data.dto.RegisterRequest
import dev.spooky.socialmediaapp.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthRepositoryImpl(
    private val http: HttpClient,
    private val baseUrl: String,
) : AuthRepository {
    lateinit var token: String

    override suspend fun login(email: String, password: String): Result<Unit> {
        val body = AuthRequest("jperez@mail.com", "12345678")
        val response = http.request("$baseUrl/auth/login") {
            method = HttpMethod.Post
            setBody(body)
        }
        if (response.status != HttpStatusCode.OK) {
            return Result.failure(Throwable(response.bodyAsText()))
        }
        val result = response.body<AuthResponse>()
        // TODO: save credentials data
        token = result.accessToken
        return getInfo()
    }

    override suspend fun getInfo(): Result<Unit> {
        val response = http.request("$baseUrl/auth/info") {
            method = HttpMethod.Get
            bearerAuth(token)
        }
        if (response.status == HttpStatusCode.OK) {
            return Result.success(Unit)
        }
        return Result.failure(Throwable(response.bodyAsText()))
    }

    override suspend fun register(username: String, displayName: String, email: String, password: String): Result<Unit> {
        val body = RegisterRequest(username, displayName, email, password)
        val response = http.request("$baseUrl/auth/register") {
            method = HttpMethod.Post
            setBody(body)
        }
        if (response.status != HttpStatusCode.OK) {
            return Result.failure(Throwable(response.bodyAsText()))
        }
        val result = response.body<AuthResponse>()
        // TODO: save credentials data
        token = result.accessToken
        return getInfo()
    }
}
