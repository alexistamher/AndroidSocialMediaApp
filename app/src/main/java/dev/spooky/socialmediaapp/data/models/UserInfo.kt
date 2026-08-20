package dev.spooky.socialmediaapp.data.models

import kotlinx.serialization.Serializable
import dev.spooky.socialmediaapp.domain.models.UserInfo as DomainUserInfo

@Serializable
data class UserInfo(
    val id: String,
    val displayName: String,
    val email: String,
    val photo: String?,
    val createdAt: Long,
)

internal fun UserInfo.toDomain(): DomainUserInfo = DomainUserInfo(id, displayName, email, photo)
