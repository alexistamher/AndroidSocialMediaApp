package dev.spooky.socialmediaapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.spooky.socialmediaapp.domain.models.Author
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.PreviewReaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.models.empty
import dev.spooky.socialmediaapp.presentation.screens.home.util.PreviewReactionsList
import dev.spooky.socialmediaapp.presentation.screens.home.util.toListType
import dev.spooky.socialmediaapp.ui.theme.SocialMediaAppTheme

@Composable
internal fun CommentsListComponent(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
    onRespondCommentPressed: (commentId: String) -> Unit,
    onReactionSelected: (targetId: String, targetType: TargetType, reactionType: ReactionType) -> Unit,
    onShowMoreCommentsPressed: (commentId: String) -> Unit,
    onDeleteCommentPressed: (commentId: String) -> Unit,
) {
    LazyColumn(modifier) {
        items(items = comments, key = { it.id }) { comment ->
            CommentPreviewItem(
                comment = comment,
                onRespondCommentPressed = onRespondCommentPressed,
                onReactionSelected = onReactionSelected,
                onShowMoreCommentsPressed = onShowMoreCommentsPressed,
                onDeleteCommentPressed = onDeleteCommentPressed,
            )
        }
    }
}

@Composable
private fun CommentChildrenListComponent(
    comments: List<Comment>,
    onRespondCommentPressed: (commentId: String) -> Unit,
    onReactionSelected: (targetId: String, targetType: TargetType, reactionType: ReactionType) -> Unit,
    onShowMoreCommentsPressed: (commentId: String) -> Unit,
    onDeleteCommentPressed: (commentId: String) -> Unit,
) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Column(Modifier.fillMaxWidth(0.9f)) {
            comments.forEach { comment ->
                CommentPreviewItem(
                    comment = comment,
                    onRespondCommentPressed = onRespondCommentPressed,
                    onReactionSelected = onReactionSelected,
                    onShowMoreCommentsPressed = onShowMoreCommentsPressed,
                    onDeleteCommentPressed = onDeleteCommentPressed,
                )
            }
        }
    }
}

@Composable
private fun CommentPreviewItem(
    comment: Comment,
    onRespondCommentPressed: (commentId: String) -> Unit,
    onReactionSelected: (targetId: String, targetType: TargetType, reactionType: ReactionType) -> Unit,
    onShowMoreCommentsPressed: (commentId: String) -> Unit,
    onDeleteCommentPressed: (commentId: String) -> Unit,
) {
    var commentOptionsVisible by remember { mutableStateOf(false) }
    Surface(
        Modifier.padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
    ) {
        Column(Modifier.padding(4.dp)) {
            Row {
                AvatarComponent(AvatarComponentSize.SMALL)
                Column {
                    Surface(
                        Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    comment.author.displayName,
                                    Modifier.weight(1f),
                                    style =
                                        MaterialTheme.typography.bodyLarge.run {
                                            copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = color.copy(0.6f),
                                            )
                                        },
                                )
                                Text("${comment.createdAt}h")
                                Box {
                                    IconButton({
                                        commentOptionsVisible = true
                                    }, Modifier.size(24.dp)) { Icon(Icons.Default.MoreHoriz, null) }
                                    DropdownMenu(commentOptionsVisible, {
                                        commentOptionsVisible = false
                                    }) {
                                        DropdownMenuItem(
                                            text = {
                                                Text("Delete comment")
                                            },
                                            leadingIcon = { Icon(Icons.Default.Delete, null) },
                                            onClick = {
                                                onDeleteCommentPressed(comment.id)
                                                commentOptionsVisible = false
                                            },
                                        )
                                    }
                                }
                            }
                            Text(comment.content)
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ReactionButtonComponent(
                            comment.previewReactions,
                            onReactionSelected = { reactionType ->
                                onReactionSelected(comment.id, TargetType.COMMENT, reactionType)
                            },
                        )
                        if (comment.commentsCount > 0) {
                            TextButton({
                                onShowMoreCommentsPressed(comment.id)
                            }, enabled = comment.commentChildren == null) {
                                Icon(Icons.Outlined.ModeComment, null)
                                Spacer(Modifier.width(4.dp))
                                Text(comment.commentsCount.toString())
                            }
                        }
                        Button({
                            onRespondCommentPressed(comment.id)
                        }, colors = ButtonDefaults.textButtonColors()) {
                            Text("reply")
                        }
                        ReactionsPreviewComponent(
                            reactions = PreviewReactionsList(comment.previewReactions).toListType(),
                        )
                    }
                }
            }

            comment.commentChildren?.let { comments ->
                CommentChildrenListComponent(
                    comments = comments,
                    onRespondCommentPressed = onRespondCommentPressed,
                    onReactionSelected = onReactionSelected,
                    onShowMoreCommentsPressed = onShowMoreCommentsPressed,
                    onDeleteCommentPressed = onDeleteCommentPressed,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCommentPreviewItem() =
    SocialMediaAppTheme {
        val randomContent = LoremIpsum(20).values.joinToString(" ")
        val comment =
            Comment.empty().copy(
                commentsCount = 1,
                content = randomContent,
                commentChildren =
                    MutableList(2) {
                        Comment.empty().copy(
                            id = "reaction-preview-id-$it",
                            content = randomContent,
                            commentChildren =
                                MutableList(3) {
                                    Comment.empty().copy(content = randomContent)
                                },
                        )
                    },
                author =
                    Author
                        .empty()
                        .copy(id = "author-preview-id-7", displayName = "JohnConnor92"),
                previewReactions =
                    listOf(
                        PreviewReaction(
                            "reaction-preview-id-1",
                            "love",
                            "target-preview-id-4",
                            "author-preview-id-5",
                        ),
                        PreviewReaction(
                            "reaction-preview-id-2",
                            "haha",
                            "target-preview-id-5",
                            "author-preview-id-6",
                        ),
                        PreviewReaction(
                            "reaction-preview-id-3",
                            "angry",
                            "target-preview-id-6",
                            "author-preview-id-7",
                        ),
                    ),
            )
        CommentPreviewItem(
            comment = comment,
            onRespondCommentPressed = {},
            onReactionSelected = { _, _, _ -> },
            onShowMoreCommentsPressed = {},
            onDeleteCommentPressed = {},
        )
    }
