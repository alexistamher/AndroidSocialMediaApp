package dev.spooky.socialmediaapp.presentation.screens.home.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoodBad
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import dev.spooky.socialmediaapp.domain.models.ReactionType

fun ReactionType.toIcon(): ImageVector =
    when (this) {
        ReactionType.SAD -> Icons.Outlined.SentimentVeryDissatisfied
        ReactionType.LOVE -> Icons.Outlined.FavoriteBorder
        ReactionType.LIKE -> Icons.Outlined.ThumbUp
        ReactionType.HAHA -> Icons.Outlined.SentimentVerySatisfied
        ReactionType.WOW -> Icons.Outlined.AutoAwesome
        ReactionType.ANGRY -> Icons.Outlined.MoodBad
    }
