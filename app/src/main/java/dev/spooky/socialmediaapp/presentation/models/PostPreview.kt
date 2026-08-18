package dev.spooky.socialmediaapp.presentation.models

data class PostPreview(
    val id: String,
    val content: String,
    val author: Author,
    val commentsCount: Int,
    val previewReactions: Map<String, Int>,
    val visibility: String,
    val createdAt: Long,
)
