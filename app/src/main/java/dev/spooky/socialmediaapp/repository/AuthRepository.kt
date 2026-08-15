package dev.spooky.socialmediaapp.repository

import dev.spooky.socialmediaapp.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }
}