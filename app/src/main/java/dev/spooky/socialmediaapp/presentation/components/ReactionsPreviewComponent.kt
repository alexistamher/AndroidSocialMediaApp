package dev.spooky.socialmediaapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.presentation.screens.home.util.toIcon

@Composable
internal fun ReactionsPreviewComponent(reactions: List<Pair<ReactionType, Int>>) {
    if (reactions.isEmpty()) return

    Row(
        Modifier
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .padding(horizontal = (reactions.size * 4).dp)
                .offset(x = (reactions.size * 4).dp)
                .widthIn(min = 30.dp),
        ) {
            reactions.sortedBy { it.second }.forEachIndexed { index, (reactionType, _) ->
                Icon(
                    reactionType.toIcon(),
                    null,
                    Modifier
                        .size(24.dp)
                        .offset(x = (-12 * index).dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                        .padding(4.dp),
                )
            }
        }
        Text(reactions.size.toString())
    }
}
