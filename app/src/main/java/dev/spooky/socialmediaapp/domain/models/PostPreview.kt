package dev.spooky.socialmediaapp.domain.models

data class PostPreview(
    val id: String,
    val content: String,
    val author: Author,
    val commentsCount: Int,
    val previewReactions: List<PreviewReaction>,
    val visibility: String,
    val createdAt: Long,
)
