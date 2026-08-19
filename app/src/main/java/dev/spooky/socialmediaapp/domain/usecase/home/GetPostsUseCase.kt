package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.PostPreview
import dev.spooky.socialmediaapp.domain.repository.PostRepository

class GetPostsUseCase(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(): Result<List<PostPreview>> = postRepository.getPostsByUserId()
}
