package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.repository.CommentRepository

class DeleteCommentUseCase(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(commentId: String): Result<Unit> = commentRepository.deleteComment(commentId)
}
