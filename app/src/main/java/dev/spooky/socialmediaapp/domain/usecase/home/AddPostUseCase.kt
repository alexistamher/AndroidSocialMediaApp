package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.PostPreview
import dev.spooky.socialmediaapp.domain.repository.PostRepository

class AddPostUseCase(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(content: String): Result<PostPreview> = postRepository.addPost(content, "public")
}
