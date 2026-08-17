package dev.spooky.socialmediaapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AuthRequest(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequest(
    val username: String,
    @SerialName("display_name")
    val displayName: String,
    val email: String,
    val password: String,
)

@Serializable
data class AuthResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)

@Serializable
data class UserInfoResponse(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("display_name")
    val displayName: String,
    val email: String,
    @SerialName("created_at")
    val createdAt: ULong,
    @SerialName("updated_at")
    val updatedAt: ULong,
    @SerialName("avatar_url")
    val avatarUrl: String?,
)
