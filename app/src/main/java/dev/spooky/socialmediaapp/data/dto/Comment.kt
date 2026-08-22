package dev.spooky.socialmediaapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Comment(
    val id: String,
    val content: String,
    val author: Author,
    @SerialName("comments_count")
    val commentsCount: Int,
    @SerialName("preview_reactions")
    val previewReactions: List<PreviewReaction>,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("post_id")
    val postId: String,
    @SerialName("parent_comment_id")
    val parentCommentId: String?,
)

@Serializable
internal data class AddCommentRequest(
    val content: String,
    @SerialName("post_id")
    val postId: String,
    @SerialName("parent_comment_id")
    val parentCommentId: String?,
)

@Serializable
internal data class GetCommentsResponse(
    val comments: List<Comment>,
)
