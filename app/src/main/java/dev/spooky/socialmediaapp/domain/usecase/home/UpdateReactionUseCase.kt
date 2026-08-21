package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.repository.ReactionRepository

class UpdateReactionUseCase(
    private val reactionRepository: ReactionRepository,
) {
    suspend operator fun invoke(
        reactionId: String,
        reactionType: ReactionType,
    ): Result<Unit> = reactionRepository.updateReaction(reactionId, reactionType)
}
