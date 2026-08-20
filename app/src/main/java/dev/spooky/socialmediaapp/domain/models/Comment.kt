package dev.spooky.socialmediaapp.domain.models

data class Comment(
    val id: String,
    val content: String,
    val author: Author,
    val previewReactions: Map<String, Int>,
    val createdAt: Long,
    val postId: String,
    val parentCommentId: String?,
)
