package dev.spooky.socialmediaapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val displayName: String,
    val email: String,
    val photo: String?,
    val createdAt: Long,
)

