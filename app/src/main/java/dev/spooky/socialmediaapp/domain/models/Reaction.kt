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

data class PreviewReaction(
    val id: String,
    val reactionType: String,
    val targetId: String,
    val authorId: String,
) {
    companion object
}

fun Reaction.Companion.empty(): Reaction = Reaction("", "", ReactionType.LIKE, 0L, Author.empty())

fun PreviewReaction.Companion.empty(): PreviewReaction = PreviewReaction("", "", "", "")

internal fun Reaction.toPreview(): PreviewReaction = PreviewReaction(id, reactionType.description, targetId, author.id)
