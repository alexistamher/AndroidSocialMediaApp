package dev.spooky.socialmediaapp.presentation.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.spooky.socialmediaapp.domain.models.Author
import dev.spooky.socialmediaapp.domain.models.Reaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.UserInfo
import dev.spooky.socialmediaapp.domain.models.empty
import dev.spooky.socialmediaapp.presentation.screens.home.util.toIcon
import dev.spooky.socialmediaapp.presentation.util.LocalUserInfo

@Composable
internal fun ReactionButtonComponent(
    reactions: List<Reaction>,
    onReactionSelected: (reactionType: ReactionType) -> Unit,
    userInfo: UserInfo = LocalUserInfo.current,
) {
    val ownReaction = reactions.firstOrNull { it.author.id == userInfo.id }
    val interactionSource = remember { MutableInteractionSource() }
    val initialReactionType = ownReaction?.reactionType ?: ReactionType.LIKE
    var reactionsOptionButtonVisible by remember { mutableStateOf(false) }

    Box(
        Modifier
            .padding(vertical = 4.dp)
            .wrapContentSize(),
    ) {
        Surface(
            shape = CircleShape,
            color = if (ownReaction != null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            contentColor =
                if (ownReaction != null) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.primary
                },
            modifier =
                Modifier
                    .clip(CircleShape)
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        onClick = { onReactionSelected(initialReactionType) },
                        onLongClick = {
                            reactionsOptionButtonVisible = true
                        },
                    ),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(initialReactionType.toIcon(), contentDescription = null)
                if (ownReaction == null) return@Row
                Text(initialReactionType.description)
            }
        }

        DropdownMenu(
            modifier = Modifier.wrapContentSize(),
            shape = CircleShape,
            expanded = reactionsOptionButtonVisible,
            onDismissRequest = { reactionsOptionButtonVisible = false },
            offset = DpOffset.Zero.copy(y = (-44).dp),
        ) {
            DropdownMenuItem(
                modifier = Modifier.width(IntrinsicSize.Max),
                text = {
                    Row(
                        Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        ReactionType.entries.forEach { type ->
                            FilledIconButton({
                                onReactionSelected(type)
                                reactionsOptionButtonVisible = false
                            }, Modifier.size(34.dp)) {
                                Icon(type.toIcon(), contentDescription = type.description)
                            }
                        }
                    }
                },
                onClick = {
                    reactionsOptionButtonVisible = false
                },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun PrivateReactionButtonComponent() {
    val reactions =
        listOf(
            Reaction.empty().copy(reactionType = ReactionType.HAHA),
            Reaction.empty().copy(reactionType = ReactionType.LIKE),
            Reaction.empty().copy(reactionType = ReactionType.LIKE),
            Reaction.empty().copy(reactionType = ReactionType.LIKE),
            Reaction.empty().copy(
                reactionType = ReactionType.LOVE,
                author = Author.empty().copy(id = "preview-id"),
            ),
            Reaction.empty().copy(reactionType = ReactionType.LOVE),
        )
    ReactionButtonComponent(reactions = reactions, onReactionSelected = {})
}
