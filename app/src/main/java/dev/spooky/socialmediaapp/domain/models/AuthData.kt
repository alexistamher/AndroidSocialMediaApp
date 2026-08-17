package dev.spooky.socialmediaapp.domain.models

data class AuthData(
    val accessToken: String,
    val refreshToken: String,
)