package dev.spooky.socialmediaapp.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun getInfo(): Result<Unit>
    suspend fun register(username: String, displayName: String, email: String, password: String): Result<Unit>
}