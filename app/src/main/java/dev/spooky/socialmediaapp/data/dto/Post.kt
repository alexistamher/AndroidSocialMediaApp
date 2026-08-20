package dev.spooky.socialmediaapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.spooky.socialmediaapp.domain.models.Author as DomainAuthor
import dev.spooky.socialmediaapp.domain.models.PostPreview as DomainPostPreview

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
internal data class Reaction(
    val id: String,
    @SerialName("target_id")
    val targetId: String,
    @SerialName("reaction_type")
    val reactionType: String,
    @SerialName("created_at")
    val createdAt: Long,
    val author: Author,
)

@Serializable
internal data class GetPostsResponse(
    val posts: List<PostPreview>,
    @SerialName("next_cursor")
    val nextCursor: Int,
)

@Serializable
internal data class AddPostResponse(
    val id: String,
    val createdAt: Long,
)

@Serializable
internal data class AddPostRequest(
    val content: String,
    val visibility: String,
    @SerialName("parent_id")
    val parentId: String?,
)

fun Author.toDomain(): DomainAuthor = DomainAuthor(id, username, displayName, avatarURL = null)

fun PostPreview.toDomain(): DomainPostPreview =
    DomainPostPreview(
        id,
        content,
        author.toDomain(),
        commentsCount ?: 0,
        previewReactions ?: emptyMap(),
        visibility,
        createdAt,
    )
