package dev.spooky.socialmediaapp.domain.repository

import dev.spooky.socialmediaapp.domain.models.Reaction

interface ReactionRepository {
    suspend fun addReaction(
        targetId: String,
        reactionType: String,
        targetType: String,
    ): Result<Reaction>

    suspend fun deleteReaction(reactionId: String): Result<Unit>
}
