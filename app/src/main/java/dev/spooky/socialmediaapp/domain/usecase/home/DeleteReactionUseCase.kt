package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.repository.ReactionRepository

class DeleteReactionUseCase(
    private val reactionRepository: ReactionRepository,
) {
    suspend operator fun invoke(reactionId: String): Result<Unit> = reactionRepository.deleteReaction(reactionId)
}
