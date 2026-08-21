package dev.spooky.socialmediaapp.presentation.screens.home.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoodBad
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import dev.spooky.socialmediaapp.domain.models.PreviewReaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.fromString

@JvmInline
internal value class PreviewReactionsList(
    val reactions: List<PreviewReaction>,
)

fun ReactionType.toIcon(): ImageVector =
    when (this) {
        ReactionType.SAD -> Icons.Outlined.SentimentVeryDissatisfied
        ReactionType.LOVE -> Icons.Outlined.FavoriteBorder
        ReactionType.LIKE -> Icons.Outlined.ThumbUp
        ReactionType.HAHA -> Icons.Outlined.SentimentVerySatisfied
        ReactionType.WOW -> Icons.Outlined.AutoAwesome
        ReactionType.ANGRY -> Icons.Outlined.MoodBad
    }

// converts List of Reactions to List of pairs of ReactionType and count
internal fun PreviewReactionsList.toListType(): List<Pair<ReactionType, Int>> =
    reactions
        .groupBy { it.reactionType }
        .map { ReactionType.fromString(it.key) to it.value.size }
