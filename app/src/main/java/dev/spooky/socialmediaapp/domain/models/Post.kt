package dev.spooky.socialmediaapp.domain.models

data class Post(
    val id: String,
    val content: String,
    val author: Author,
    val previewReactions: List<PreviewReaction>,
    val visibility: String,
    val createdAt: Long,
)
