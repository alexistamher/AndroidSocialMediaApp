package dev.spooky.socialmediaapp.domain.models

data class Post(
    val id: String,
    val content: String,
    val author: Author,
    val reactions: List<Reaction>,
    val visibility: String,
    val createdAt: Long,
)

data class Reaction(
    val id: String,
    val targetId: String,
    val reactionType: String,
    val createdAt: Long,
    val author: Author,
) {
    companion object
}

fun Reaction.Companion.empty(): Reaction = Reaction("", "", "", 0L, Author.empty())
