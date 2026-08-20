package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.Reaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.repository.ReactionRepository

class AddReactionUseCase(
    private val reactionRepository: ReactionRepository,
) {
    suspend operator fun invoke(
        targetId: String,
        reactionType: ReactionType,
        targetType: TargetType,
    ): Result<Reaction> = reactionRepository.addReaction(targetId, reactionType.description, targetType.description)
}
