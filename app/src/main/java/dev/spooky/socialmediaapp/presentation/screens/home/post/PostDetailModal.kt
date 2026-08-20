package dev.spooky.socialmediaapp.presentation.screens.home.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.spooky.socialmediaapp.domain.models.Author
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.Post
import dev.spooky.socialmediaapp.domain.models.Reaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.models.empty
import dev.spooky.socialmediaapp.presentation.screens.home.util.toIcon
import dev.spooky.socialmediaapp.presentation.util.LocalUserInfo
import dev.spooky.socialmediaapp.ui.theme.SocialMediaAppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostDetailModal(
    postId: String?,
    onCloseModal: () -> Unit,
    viewModel: PostDetailViewModel = koinViewModel<PostDetailViewModel>(),
) {
    if (postId == null) return
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPost(postId)
    }

    if (state.post == null) return
    ModalBottomSheet(
        {
            viewModel.reset()
            onCloseModal()
        },
        Modifier.systemBarsPadding(),
        sheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            ),
        contentWindowInsets = {
            WindowInsets.ime
        },
    ) {
        Column {
            PostDetailModalContent(
                post = state.post!!,
                onReactionSelected = { targetId, targetType, reactionType ->
                    viewModel.toggleReaction(targetId, targetType, reactionType)
                },
            )
            CommentsSection(comments = state.comments, onRespondCommentPressed = { commentId ->
                viewModel.setCommentId(commentId)
            })
            state.selectedComment?.let { comment ->
                SelectedCommentSection(comment = comment, onCloseSelectedCommentPressed = {
                    viewModel.setCommentId(
                        null,
                    )
                })
            }
            CommentTextField(onAddComment = { content ->
                viewModel.addComment(content)
            })
        }
    }
}

@Composable
private fun CommentTextField(onAddComment: (content: String) -> Unit) {
    var commentContent: String by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Row(
        Modifier
            .imePadding()
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            commentContent,
            { value -> commentContent = value },
            Modifier.weight(1f),
            shape = CircleShape,
            trailingIcon = {
                Icon(
                    Icons.AutoMirrored.Outlined.Send,
                    null,
                    Modifier.clickable(enabled = commentContent.isNotEmpty(), onClick = {
                        onAddComment(commentContent)
                        commentContent = ""
                        focusManager.clearFocus()
                    }),
                )
            },
        )
    }
}

@Composable
private fun SelectedCommentSection(
    comment: Comment,
    onCloseSelectedCommentPressed: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            ).padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                buildAnnotatedString {
                    append("Reply to ")
                    pushStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)))
                    append("@${comment.author.username}")
                },
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            IconButton(onCloseSelectedCommentPressed, Modifier.size(30.dp)) {
                Icon(Icons.Default.Clear, null)
            }
        }
        Text(comment.content, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ColumnScope.CommentsSection(
    comments: List<Comment>,
    onRespondCommentPressed: (commentId: String) -> Unit,
) {
    LazyColumn(
        Modifier
            .weight(1f)
            .padding(8.dp),
    ) {
        items(comments) { comment ->
            CommentPreviewItem(comment, onRespondCommentPressed)
        }
    }
}

@Composable
private fun CommentPreviewItem(
    comment: Comment,
    onRespondCommentPressed: (commentId: String) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            Modifier
                .padding(8.dp)
                .size(60.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.Face,
                null,
                Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        Column {
            Text(comment.author.displayName)
            Text(comment.content)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("${comment.createdAt}h")
                Button({
                    onRespondCommentPressed(comment.id)
                }, colors = ButtonDefaults.textButtonColors()) {
                    Text("reply")
                }
            }
        }
    }
}

@Composable
private fun ReactionsSection(reactions: List<Reaction>) {
    if (reactions.isEmpty()) return
    val grouped = reactions.groupBy { it.reactionType }.map { it.key to it.value.size }

    Box(
        Modifier
            .padding(horizontal = 8.dp)
            .widthIn(min = 30.dp),
        contentAlignment = Alignment.Center,
    ) {
        grouped.sortedBy { it.second }.forEachIndexed { index, (reactionType, _) ->
            Icon(
                reactionType.toIcon(),
                null,
                Modifier
                    .size(18.dp)
                    .offset(x = (-12 * index).dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape),
            )
        }
    }
}

@Composable
private fun PostDetailModalContent(
    post: Post,
    onReactionSelected: (targetId: String, targetType: TargetType, reactionType: ReactionType) -> Unit,
) {
    Column {
        Text(post.content, Modifier.padding(8.dp), style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReactionButton(
                post.reactions,
                onReactionSelected = { reactionType ->
                    onReactionSelected(
                        post.id,
                        TargetType.POST,
                        reactionType,
                    )
                },
            )
            Spacer(Modifier.weight(1f))
            ReactionsSection(post.reactions)
        }
        HorizontalDivider()
    }
}

@Composable
private fun ReactionButton(
    reactions: List<Reaction>,
    onReactionSelected: (reactionType: ReactionType) -> Unit,
) {
    val userInfo = LocalUserInfo.current
    val ownReaction = reactions.firstOrNull { it.author.id == userInfo.id }
    val reactionType = ownReaction?.reactionType ?: ReactionType.LIKE
    val buttonColors =
        ownReaction?.let {
            ButtonDefaults.textButtonColors(MaterialTheme.colorScheme.onPrimary)
        } ?: ButtonDefaults.textButtonColors()

    TextButton(
        { onReactionSelected(reactionType) },
        colors = buttonColors,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(reactionType.toIcon(), null)
            Text(reactionType.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPostDetailModalContent() =
    SocialMediaAppTheme {
        val post =
            Post(
                "",
                LoremIpsum(50).values.joinToString(" "),
                Author.empty(),
                listOf(
                    Reaction.empty().copy(reactionType = ReactionType.HAHA),
                    Reaction.empty().copy(reactionType = ReactionType.LIKE),
                    Reaction.empty().copy(reactionType = ReactionType.LIKE),
                    Reaction.empty().copy(reactionType = ReactionType.LIKE),
                    Reaction.empty().copy(reactionType = ReactionType.LOVE),
                    Reaction.empty().copy(reactionType = ReactionType.LOVE),
                ),
                "public",
                0L,
            )
        PostDetailModalContent(post = post, onReactionSelected = { _, _, _ -> })
    }

@Preview(showBackground = true)
@Composable
private fun PreviewCommentPreviewItem() =
    SocialMediaAppTheme {
        val comment =
            Comment.empty().copy(
                author = Author.empty().copy(displayName = "JohnConnor92"),
            )
        CommentPreviewItem(comment = comment, onRespondCommentPressed = {})
    }

@Preview(showBackground = true)
@Composable
private fun PreviewSelectedCommentSection() {
    val comment =
        Comment.empty().copy(
            content = LoremIpsum(30).values.joinToString(" "),
            author = Author.empty().copy(displayName = "JohnConnor92", username = "jconnor92"),
        )
    SelectedCommentSection(comment = comment, onCloseSelectedCommentPressed = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewCommentTextField() {
    CommentTextField(onAddComment = {})
}
