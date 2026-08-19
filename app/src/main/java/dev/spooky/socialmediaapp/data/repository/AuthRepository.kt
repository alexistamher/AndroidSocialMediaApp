package dev.spooky.socialmediaapp.data.repository

import dev.spooky.socialmediaapp.data.dto.AuthRequest
import dev.spooky.socialmediaapp.data.dto.AuthResponse
import dev.spooky.socialmediaapp.data.dto.RegisterRequest
import dev.spooky.socialmediaapp.data.dto.UserInfoResponse
import dev.spooky.socialmediaapp.data.models.AuthData
import dev.spooky.socialmediaapp.data.models.UserInfo
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

class AuthRepositoryImpl(
    private val http: HttpClient,
    private val baseUrl: String,
    private val sessionHelper: SessionHelper,
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit> {
        val body = AuthRequest(email, password)
        val response =
            http.request("$baseUrl/auth/login") {
                method = HttpMethod.Post
                setBody(body)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failure(Throwable("invalid credentials"))
        }
        val result = response.body<AuthResponse>()
        sessionHelper.setAuth(AuthData(result.accessToken, result.refreshToken))
        return getInfo()
    }

    override suspend fun getInfo(): Result<Unit> {
        val auth = sessionHelper.getAuth() ?: return Result.failure(Throwable("unauthorized"))
        val response =
            http.request("$baseUrl/auth/info") {
                method = HttpMethod.Get
                bearerAuth(auth.accessToken)
            }
        if (response.status == HttpStatusCode.OK) {
            val body = response.body<UserInfoResponse>()
            val info = body.run { UserInfo(id, displayName, email, null, body.createdAt) }
            sessionHelper.setUserInfo(info)
            return Result.success(Unit)
        }
        return Result.failure(Throwable("user data not available"))
    }

    override suspend fun register(
        username: String,
        displayName: String,
        email: String,
        password: String,
    ): Result<Unit> {
        val body = RegisterRequest(username, displayName, email, password)
        val response =
            http.request("$baseUrl/auth/register") {
                method = HttpMethod.Post
                setBody(body)
            }
        if (response.status != HttpStatusCode.Created) {
            return Result.failure(Throwable("error on register attempting"))
        }
        val result = response.body<AuthResponse>()
        sessionHelper.setAuth(AuthData(result.accessToken, result.refreshToken))
        return getInfo()
    }
}
