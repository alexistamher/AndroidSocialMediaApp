package dev.spooky.socialmediaapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.spooky.socialmediaapp.domain.models.Author
import dev.spooky.socialmediaapp.domain.models.PostPreview
import dev.spooky.socialmediaapp.presentation.util.ScreenState
import dev.spooky.socialmediaapp.presentation.util.asError
import dev.spooky.socialmediaapp.presentation.util.success
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    onLogout: () -> Unit,
    onPostPressed: (postId: String) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }

    Scaffold(
        Modifier.semantics {
            testTagsAsResourceId = true
            testTag = "home_screen"
        },
        topBar = {
            TopAppBar(title = {
                Text(
                    "Home",
                    style = MaterialTheme.typography.titleLarge,
                )
            })
        },
    ) { mainPadding ->
        when (state) {
            is ScreenState.Error -> {
                Box(
                    Modifier
                        .testTag("home:content_error_message")
                        .fillMaxSize(),
                    Alignment.Center,
                ) {
                    Text(state.asError())
                }
            }

            ScreenState.Idle, is ScreenState.Loading -> {
                LinearProgressIndicator(
                    Modifier
                        .testTag("home:progress_bar")
                        .fillMaxWidth()
                        .systemBarsPadding(),
                )
            }

            is ScreenState.Success -> {
                with(state.success()) {
                    Column(
                        Modifier
                            .padding(mainPadding)
                            .fillMaxSize(),
                    ) {
                        PostFieldSection()
                        if (posts.isEmpty()) {
                            Box(
                                Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("You don't have posts yet")
                            }
                            return@Column
                        }
                        PostsContent(
                            posts = state.success().posts,
                            Modifier
                                .weight(1f),
                            onDeletePostPressed = { postId ->
                                viewModel.deletePost(postId)
                            },
                            onPostPressed = onPostPressed,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PostsContent(
    posts: List<PostPreview>,
    modifier: Modifier,
    onDeletePostPressed: (id: String) -> Unit,
    onPostPressed: (id: String) -> Unit = {},
) {
    LazyColumn(
        modifier
            .testTag("home:posts_content")
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(
            items = posts,
            key = { _, item -> item.id },
        ) { idx, post ->
            PostPreviewItem(
                post = post,
                onDeletePostPressed = onDeletePostPressed,
                onPostPressed = onPostPressed,
            )
            if (idx < posts.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun PostPreviewItem(
    post: PostPreview,
    onDeletePostPressed: (id: String) -> Unit,
    onPostPressed: (id: String) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onPostPressed(post.id) }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            Modifier
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
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(post.author.displayName, Modifier.weight(1f))
                    Text("@${post.author.username}")
                    Text("2h")
                }
                Box {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete post") },
                            onClick = { onDeletePostPressed(post.id) },
                        )
                    }
                }
            }
            Text(post.content)
            Row(
                Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, null)
                    Text(post.previewReactions.size.toString())
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Outlined.ModeComment, null)
                    Text(post.previewReactions.size.toString())
                }
            }
        }
    }
}

@Composable
private fun PostFieldSection() {
    val viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
    var content by remember { mutableStateOf("") }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        TextField(
            content,
            {
                content = it
            },
            Modifier.fillMaxWidth(),
            placeholder = { Text("what are you thinking about?") },
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                {
                    viewModel.addPost(content)
                },
                enabled = content.isNotEmpty(),
            ) {
                Text("post it!")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPostFieldSection() =
    Box(Modifier.padding(20.dp)) {
        PostFieldSection()
    }

@Preview(showBackground = true)
@Composable
private fun PreviewPostsContent() {
    val posts =
        MutableList(20) {
            val wordsSize = Random.nextInt(40) + 10
            val content = LoremIpsum(words = wordsSize).values.joinToString()
            val commentsSize = Random.nextInt(5)
            PostPreview(
                it.toString(),
                content,
                Author("$it-$it", "test user $it", "TestUser$it", null),
                commentsSize,
                emptyMap(),
                "public",
                0L,
            )
        }
    PostsContent(posts, Modifier, onDeletePostPressed = {})
}

@Composable
@Preview(showBackground = true)
private fun PreviewPostPreviewItem() {
    val content = LoremIpsum(words = 30).values.joinToString()
    val post =
        PostPreview(
            "test-post-id",
            content,
            Author(
                "test-author-test",
                "test user",
                "TestUser",
                null,
            ),
            0,
            mapOf(
                "like" to 3,
                "haha" to 2,
                "angry" to 1,
            ),
            "public",
            0L,
        )
    PostPreviewItem(post, onDeletePostPressed = {})
}
