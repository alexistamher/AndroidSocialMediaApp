package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.models.Post
import dev.spooky.socialmediaapp.domain.repository.PostRepository

class GetPostByIdUseCase(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(postId: String): Result<Post> = postRepository.getPostById(postId)
}
