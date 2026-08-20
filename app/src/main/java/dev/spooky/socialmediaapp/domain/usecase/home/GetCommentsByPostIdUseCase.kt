package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.repository.CommentRepository

class GetCommentsByPostIdUseCase(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(postId: String): Result<List<Comment>> = commentRepository.getCommentsByPostId(postId)
}
