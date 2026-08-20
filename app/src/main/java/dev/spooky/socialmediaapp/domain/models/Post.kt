package dev.spooky.socialmediaapp.domain.models

data class Post(
    val id: String,
    val content: String,
    val author: Author,
    val reactions: List<Reaction>,
    val visibility: String,
    val createdAt: Long,
)
