package dev.spooky.socialmediaapp.data.models

import kotlinx.serialization.Serializable
import dev.spooky.socialmediaapp.domain.models.AuthData as DomainAuthData

@Serializable
data class AuthData(
    val accessToken: String,
    val refreshToken: String,
)

fun AuthData.toDomain(): DomainAuthData = DomainAuthData(accessToken, refreshToken)
