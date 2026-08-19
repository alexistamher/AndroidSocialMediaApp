package dev.spooky.socialmediaapp.domain.models

data class Author(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarURL: String?,
) {
    companion object {
        fun empty() = Author("", "", "", null)
    }
}
