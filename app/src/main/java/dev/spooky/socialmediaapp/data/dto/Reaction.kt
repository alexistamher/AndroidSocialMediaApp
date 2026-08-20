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
internal data class AddReactionRequest(
    @SerialName("target_id")
    val targetId: String,
    @SerialName("reaction_type")
    val reactionType: String,
    @SerialName("reaction_target_type")
    val targetType: String,
)
