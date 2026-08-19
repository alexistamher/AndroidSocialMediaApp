package dev.spooky.socialmediaapp.domain.usecase.home

import dev.spooky.socialmediaapp.domain.repository.PostRepository

class DeletePostUseCase(
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(postId: String): Result<Unit> = postRepository.deletePost(postId)
}
