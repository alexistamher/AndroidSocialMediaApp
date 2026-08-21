package dev.spooky.socialmediaapp.domain.repository

import dev.spooky.socialmediaapp.domain.models.Reaction
import dev.spooky.socialmediaapp.domain.models.ReactionType

interface ReactionRepository {
    suspend fun addReaction(
        targetId: String,
        reactionType: String,
        targetType: String,
    ): Result<Reaction>

    suspend fun deleteReaction(reactionId: String): Result<Unit>

    suspend fun updateReaction(
        reactionId: String,
        reactionType: ReactionType,
    ): Result<Unit>
}
