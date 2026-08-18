package dev.spooky.socialmediaapp.domain.usecase.auth

import dev.spooky.socialmediaapp.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        username: String,
        displayName: String,
        email: String,
        password: String,
    ): Result<Unit> = repository.register(username, displayName, email, password)
}
