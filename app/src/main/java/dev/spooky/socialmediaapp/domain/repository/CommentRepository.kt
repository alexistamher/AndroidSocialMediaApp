package dev.spooky.socialmediaapp.domain.repository

import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.TargetType

interface CommentRepository {
    suspend fun getCommentsByTargetId(
        targetId: String,
        targetType: TargetType,
    ): Result<List<Comment>>

    suspend fun addComment(
        content: String,
        postId: String,
        parentCommentId: String?,
    ): Result<Comment>

    suspend fun deleteComment(commentId: String): Result<Unit>
}
