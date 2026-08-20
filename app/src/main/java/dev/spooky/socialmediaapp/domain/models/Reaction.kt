package dev.spooky.socialmediaapp.domain.models

data class Reaction(
    val id: String,
    val targetId: String,
    val reactionType: ReactionType,
    val createdAt: Long,
    val author: Author,
) {
    companion object
}

fun Reaction.Companion.empty(): Reaction = Reaction("", "", ReactionType.LIKE, 0L, Author.empty())
