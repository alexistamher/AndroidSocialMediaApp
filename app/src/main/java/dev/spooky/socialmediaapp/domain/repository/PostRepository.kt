package dev.spooky.socialmediaapp.domain.repository

import dev.spooky.socialmediaapp.domain.models.PostPreview

interface PostRepository {
    suspend fun getPostsByUserId(): Result<List<PostPreview>>

    suspend fun addPost(
        content: String,
        visibility: String,
    ): Result<PostPreview>

    suspend fun deletePost(postId: String): Result<Unit>
}
