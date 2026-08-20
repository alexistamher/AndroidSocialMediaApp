package dev.spooky.socialmediaapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("author")
data class Author(
    val id: String,
    val username: String,
    @SerialName("display_name")
    val displayName: String,
)

@Serializable
data class PostPreview(
    val id: String,
    val content: String,
    val author: Author,
    @SerialName("comments_count")
    val commentsCount: Int?,
    @SerialName("preview_reactions")
    val previewReactions: Map<String, Int>?,
    val visibility: String,
    @SerialName("created_at")
    val createdAt: Long,
)

@Serializable
internal data class Post(
    val id: String,
    val content: String,
    val author: Author,
    val reactions: List<Reaction>,
    val visibility: String,
    @SerialName("created_at")
    val createdAt: Long,
)

@Serializable
internal data class GetPostsResponse(
    val posts: List<PostPreview>,
    @SerialName("next_cursor")
    val nextCursor: Int,
)

@Serializable
internal data class AddPostRequest(
    val content: String,
    val visibility: String,
    @SerialName("parent_id")
    val parentId: String?,
)
