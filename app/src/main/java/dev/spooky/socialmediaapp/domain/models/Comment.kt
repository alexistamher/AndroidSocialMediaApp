package dev.spooky.socialmediaapp.domain.models

data class Comment(
    val id: String,
    val content: String,
    val author: Author,
    val commentsCount: Int,
    val previewReactions: List<PreviewReaction>,
    val commentChildren: List<Comment>?,
    val createdAt: Long,
    val postId: String,
    val parentCommentId: String?,
) {
    companion object
}

fun Comment.Companion.empty() = Comment("", "", Author.empty(), 0, emptyList(), null, 0L, "", null)
