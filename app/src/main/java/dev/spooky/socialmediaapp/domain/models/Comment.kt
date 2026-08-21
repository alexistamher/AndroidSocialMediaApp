package dev.spooky.socialmediaapp.domain.models

data class Comment(
    val id: String,
    val content: String,
    val author: Author,
    val previewReactions: List<PreviewReaction>,
    val createdAt: Long,
    val postId: String,
    val parentCommentId: String?,
) {
    companion object
}

fun Comment.Companion.empty() = Comment("", "", Author.empty(), emptyList(), 0L, "", null)
