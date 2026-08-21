package dev.spooky.socialmediaapp.presentation.screens.home.post

import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.Post
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.usecase.home.AddCommentUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetCommentsByPostIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostByIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.ToggleReactionUseCase
import dev.spooky.socialmediaapp.presentation.screens.BaseViewModel
import kotlinx.coroutines.launch

internal class PostDetailViewModel(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getCommentsByPostIdUseCase: GetCommentsByPostIdUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val toggleReactionUseCase: ToggleReactionUseCase,
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
            val result = addCommentUseCase(comment, postId, currentState.selectedComment?.id)
            if (result.isFailure) return@launch
            val comment = result.getOrNull() ?: return@launch
            setState { current ->
                current.copy(
                    comments = current.comments + comment,
                    selectedComment = null,
                )
            }
        }
    }

    fun setCommentId(commentId: String?) {
        if (commentId == null) {
            setState { current -> current.copy(selectedComment = null) }
            return
        }
        val comment = currentState.comments.firstOrNull { it.id == commentId } ?: return
        setState { current -> current.copy(selectedComment = comment) }
    }

    fun toggleReaction(
        targetId: String,
        targetType: TargetType,
        reactionType: ReactionType,
    ) {
        val post = currentState.post ?: return
        viewModelScope.launch {
            val result =
                toggleReactionUseCase(
                    targetId,
                    targetType,
                    reactionType,
                    post.previewReactions,
                )
            if (result.isFailure) return@launch
            val newReactions = result.getOrNull() ?: return@launch
            val newPost = post.copy(previewReactions = newReactions)
            setState { currentState.copy(post = newPost) }
        }
    }

    fun reset() {
        setState { PostDetailState() }
    }
}

internal data class PostDetailState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val selectedComment: Comment? = null,
)
