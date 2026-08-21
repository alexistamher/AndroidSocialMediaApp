package dev.spooky.socialmediaapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
data class PreviewReaction(
    val id: String,
    @SerialName("reaction_type")
    val reactionType: String,
    @SerialName("target_id")
    val targetId: String,
    @SerialName("author_id")
    val authorId: String,
)

@Serializable
internal data class AddReactionRequest(
    @SerialName("target_id")
    val targetId: String,
    @SerialName("reaction_type")
    val reactionType: String,
    @SerialName("reaction_target_type")
    val targetType: String,
)

@Serializable
internal data class UpdateReactionRequest(
    @SerialName("id")
    val reactionId: String,
    @SerialName("reaction_type")
    val reactionType: String,
)
