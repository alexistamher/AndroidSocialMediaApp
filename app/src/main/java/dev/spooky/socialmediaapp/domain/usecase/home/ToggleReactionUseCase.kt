package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.core.util.error
import dev.spooky.socialmediaapp.core.util.failed
import dev.spooky.socialmediaapp.domain.models.PreviewReaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.models.toPreview

class ToggleReactionUseCase(
    private val deleteReactionUseCase: DeleteReactionUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val updateReactionUseCase: UpdateReactionUseCase,
) {
    suspend operator fun invoke(
        targetId: String,
        targetType: TargetType,
        reactionType: ReactionType,
        previewReactions: List<PreviewReaction>,
    ): Result<List<PreviewReaction>> {
        val reactionIdx = previewReactions.indexOfFirst { pr -> pr.targetId == targetId }
        if (reactionIdx == -1) {
            val result = addReactionUseCase(targetId, reactionType, targetType)
            if (result.isFailure) return Result.failed(result.error())
            val reaction =
                result.getOrNull() ?: return Result.failed("failed on obtaining reaction")
            return Result.success(listOf(reaction.toPreview()))
        }
        val previewReaction = previewReactions[reactionIdx]
        if (previewReaction.reactionType == reactionType.description) {
            val result = deleteReactionUseCase(previewReaction.id)
            if (result.isFailure) return Result.failed(result.error())
            val newReactions = previewReactions.filterNot { it.id == previewReaction.id }
            return Result.success(newReactions)
        }
        val result = updateReactionUseCase(previewReaction.id, reactionType)
        if (result.isFailure) return Result.failed(result.error())
        val newReactions =
            previewReactions.toMutableList().apply {
                set(reactionIdx, previewReaction.copy(reactionType = reactionType.description))
            }
        return Result.success(newReactions.toList())
    }
}
