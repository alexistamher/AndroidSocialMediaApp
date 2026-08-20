package dev.spooky.socialmediaapp.presentation.screens.home.post

import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.domain.usecase.home.AddCommentUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetCommentsByPostIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostByIdUseCase
import dev.spooky.socialmediaapp.presentation.screens.BaseViewModel
import kotlinx.coroutines.launch

internal class PostDetailViewModel(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getCommentsByPostIdUseCase: GetCommentsByPostIdUseCase,
    private val addCommentUseCase: AddCommentUseCase,
) : BaseViewModel<PostDetailState>() {
    override fun initialState(): PostDetailState = PostDetailState()

    fun getPost(postId: String) {
        viewModelScope
            .launch {
                val result = getPostByIdUseCase(postId)
                if (result.isFailure) return@launch
                val post = result.getOrNull() ?: return@launch
                setState { current -> current.copy(post = post) }
                    .join()
                getComments()
            }
    }

    fun getComments() {
        viewModelScope.launch {
            val postId = currentState.post?.id ?: return@launch
            val result = getCommentsByPostIdUseCase(postId)
            if (result.isFailure) return@launch
            val comments = result.getOrNull() ?: return@launch
            setState { current -> current.copy(comments = comments) }
        }
    }

    fun addComment(comment: String) {
        viewModelScope.launch {
            val postId = currentState.post?.id ?: return@launch
            val result = addCommentUseCase(comment, postId, currentState.selectedCommentId)
            if (result.isFailure) return@launch
            val comment = result.getOrNull() ?: return@launch
            setState { current -> current.copy(comments = current.comments + comment) }
        }
    }
}
