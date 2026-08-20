package dev.spooky.socialmediaapp.domain.models

data class UserInfo(
    val id: String,
    val displayName: String,
    val email: String,
    val photo: String?,
) {
    companion object
}

fun UserInfo.Companion.empty() = UserInfo("", "", "", null)
