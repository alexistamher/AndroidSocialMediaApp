package dev.spooky.socialmediaapp.presentation.util

import androidx.compose.runtime.compositionLocalOf
import dev.spooky.socialmediaapp.domain.models.UserInfo
import dev.spooky.socialmediaapp.domain.models.empty

internal val LocalUserInfo = compositionLocalOf { UserInfo.empty() }
