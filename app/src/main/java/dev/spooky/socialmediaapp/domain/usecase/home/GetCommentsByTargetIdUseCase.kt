package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.repository.CommentRepository

class GetCommentsByTargetIdUseCase(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(
        targetId: String,
        targetType: TargetType = TargetType.POST,
    ): Result<List<Comment>> = commentRepository.getCommentsByTargetId(targetId, targetType)
}
