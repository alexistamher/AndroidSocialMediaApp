package dev.spooky.socialmediaapp.presentation.screens.home.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.spooky.socialmediaapp.domain.models.Author
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.Post
import dev.spooky.socialmediaapp.domain.models.Reaction
import dev.spooky.socialmediaapp.domain.models.empty
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
    val state by viewModel.state.collectAsStateWithLifecycle()
    var commentContent: String by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getPost(postId)
    }

    state.post?.let { post ->
        ModalBottomSheet(
            onCloseModal,
            sheetState =
                rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                ),
        ) {
            Column {
                PostDetailModalContent(post)
                Column(Modifier.padding(8.dp)) {
                    state.comments.forEach {
                        Text(it.content)
                    }
                }
                Row(Modifier.fillMaxWidth()) {
                    TextField(
                        commentContent,
                        { value -> commentContent = value },
                        Modifier.weight(1f),
                        enabled = commentContent.isEmpty(),
                    )
                    IconButton({
                        viewModel.addComment(commentContent)
                        commentContent = ""
                    }) {
                        Icon(Icons.AutoMirrored.Outlined.Send, null)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReactionsSection(reactions: List<Reaction>) {
    if (reactions.isEmpty()) return
    val firstReaction = reactions.first()
    val grouped = reactions.groupBy { it.reactionType }.map { it.key to it.value.size }
    Row {
        grouped.forEach { (reactionType, _) ->
            Text(reactionType)
        }
        Text(firstReaction.author.username)
    }
}

@Composable
private fun PostDetailModalContent(post: Post) {
    Column {
        Text(post.content, Modifier.padding(8.dp), style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth()) {
            Button(
                {},
                colors = ButtonDefaults.textButtonColors(),
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Outlined.ThumbUp, null)
                    Text("like")
                }
            }
            ReactionsSection(post.reactions)
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
                    Reaction.empty().copy(reactionType = "haha"),
                    Reaction.empty().copy(reactionType = "like"),
                    Reaction.empty().copy(reactionType = "love"),
                    Reaction.empty().copy(reactionType = "love"),
                ),
                "public",
                0L,
            )
        PostDetailModalContent(post)
    }

internal data class PostDetailState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val selectedCommentId: String? = null,
)
