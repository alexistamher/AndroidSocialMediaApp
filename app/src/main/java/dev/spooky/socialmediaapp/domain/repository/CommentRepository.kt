package dev.spooky.socialmediaapp.domain.repository

import dev.spooky.socialmediaapp.domain.models.Comment

interface CommentRepository {
    suspend fun getCommentsByPostId(postId: String): Result<List<Comment>>

    suspend fun addComment(
        content: String,
        postId: String,
        parentCommentId: String?,
    ): Result<Comment>
}
