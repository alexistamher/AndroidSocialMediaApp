package dev.spooky.socialmediaapp.domain.usecase.auth

import dev.spooky.socialmediaapp.data.util.SessionHelper

class LogoutUseCase(
    private val sessionHelper: SessionHelper,
) {
    suspend operator fun invoke(): Result<Unit> {
        sessionHelper.reset()
        return Result.success(Unit)
    }
}
