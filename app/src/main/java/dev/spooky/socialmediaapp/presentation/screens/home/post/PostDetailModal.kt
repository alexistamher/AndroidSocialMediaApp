package dev.spooky.socialmediaapp.presentation.screens.home.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.spooky.socialmediaapp.domain.models.Author
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.Post
import dev.spooky.socialmediaapp.domain.models.PreviewReaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.models.empty
import dev.spooky.socialmediaapp.presentation.components.AvatarComponent
import dev.spooky.socialmediaapp.presentation.components.CommentsListComponent
import dev.spooky.socialmediaapp.presentation.components.ReactionButtonComponent
import dev.spooky.socialmediaapp.presentation.components.ReactionsPreviewComponent
import dev.spooky.socialmediaapp.presentation.screens.home.util.PreviewReactionsList
import dev.spooky.socialmediaapp.presentation.screens.home.util.toListType
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
                commentsSize = state.comments.size,
                onReactionSelected = { targetId, targetType, reactionType ->
                    viewModel.toggleReaction(targetId, targetType, reactionType)
                },
            )
            CommentsListComponent(
                Modifier.weight(1f),
                comments = state.comments,
                onRespondCommentPressed = { commentId ->
                    viewModel.setCommentId(commentId)
                },
                onReactionSelected = { targetId, targetType, reactionType ->
                    viewModel.toggleReaction(targetId, targetType, reactionType)
                },
                onShowMoreCommentsPressed = { commentId ->
                    viewModel.getCommentComments(commentId)
                },
            )
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
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
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
            IconButton(onCloseSelectedCommentPressed, Modifier.size(24.dp)) {
                Icon(Icons.Default.Clear, null)
            }
        }
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            VerticalDivider(thickness = 4.dp)
            Text(
                comment.content,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun PostDetailModalContent(
    post: Post,
    commentsSize: Int,
    onReactionSelected: (targetId: String, targetType: TargetType, reactionType: ReactionType) -> Unit,
) {
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AvatarComponent()
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        post.author.displayName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                    Text(
                        "@${post.author.username}",
                        style =
                            MaterialTheme.typography.bodyLarge.run {
                                copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = color.copy(alpha = 0.6f),
                                )
                            },
                    )
                }
                Text("${post.createdAt}hrs")
            }
        }
        Text(
            post.content,
            Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        HorizontalDivider(Modifier.padding(horizontal = 8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReactionButtonComponent(
                post.previewReactions,
                onReactionSelected = { reactionType ->
                    onReactionSelected(
                        post.id,
                        TargetType.POST,
                        reactionType,
                    )
                },
            )
            Spacer(Modifier.weight(1f))
            if (commentsSize > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Outlined.ModeComment, null)
                    Text(commentsSize.toString())
                }
            }
            ReactionsPreviewComponent(
                PreviewReactionsList(post.previewReactions).toListType(),
            )
        }
        HorizontalDivider(Modifier.padding(horizontal = 8.dp))
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
                Author.empty().copy(displayName = "John Connor", username = "jconnor92"),
                listOf(
                    PreviewReaction.empty().copy(reactionType = ReactionType.HAHA.description),
                    PreviewReaction.empty().copy(reactionType = ReactionType.LIKE.description),
                    PreviewReaction.empty().copy(reactionType = ReactionType.LIKE.description),
                    PreviewReaction.empty().copy(reactionType = ReactionType.LIKE.description),
                    PreviewReaction.empty().copy(reactionType = ReactionType.LOVE.description),
                    PreviewReaction.empty().copy(reactionType = ReactionType.LOVE.description),
                ),
                "public",
                12L,
            )
        PostDetailModalContent(post = post, commentsSize = 3, onReactionSelected = { _, _, _ -> })
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
