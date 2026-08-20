package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.repository.CommentRepository

class AddCommentUseCase(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(
        content: String,
        postId: String,
        commentParentId: String?,
    ): Result<Comment> = commentRepository.addComment(content, postId, commentParentId)
}
