package dev.spooky.socialmediaapp.domain.usecase.auth

import dev.spooky.socialmediaapp.domain.repository.AuthRepository

class LoginUseCase(
    val repository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<Unit> = repository.login(email, password)
}
