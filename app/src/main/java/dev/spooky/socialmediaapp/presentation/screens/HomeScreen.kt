package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.spooky.socialmediaapp.presentation.models.Author
import dev.spooky.socialmediaapp.presentation.models.PostPreview
import kotlin.random.Random

@Composable
fun HomeScreen() {
    val posts = MutableList(20) {
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
    Scaffold { mainPadding ->
        LazyColumn(
            Modifier
                .padding(mainPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    "Home",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            itemsIndexed(items = posts) { idx, post ->
                PostPreviewItem(post)
                if (idx < posts.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}

@Composable
private fun PostPreviewItem(post: PostPreview) {
    Row(
        Modifier
            .fillMaxWidth()
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(post.author.displayName)
                Text("@${post.author.username}", Modifier.weight(1f))
                Text("2h")
                FilledTonalIconButton({}, Modifier.size(28.dp), colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )) {
                    Icon(Icons.Default.MoreHoriz, null)
                }
            }
            Text(post.content)
            Row(
                Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, null)
                    Text(post.previewReactions.size.toString())
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Outlined.ModeComment, null)
                    Text(post.previewReactions.size.toString())
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewPostPreviewItem() {
    val content = LoremIpsum(words = 30).values.joinToString()
    val post = PostPreview(
        "test-post-id", content,
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
        "public", 0L,
    )
    PostPreviewItem(post)
}

