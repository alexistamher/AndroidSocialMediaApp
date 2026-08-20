package dev.spooky.socialmediaapp.presentation.screens.home.post

import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.Post
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.usecase.home.AddCommentUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.AddReactionUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.DeleteReactionUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetCommentsByPostIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostByIdUseCase
import dev.spooky.socialmediaapp.presentation.screens.BaseViewModel
import kotlinx.coroutines.launch

internal class PostDetailViewModel(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getCommentsByPostIdUseCase: GetCommentsByPostIdUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase,
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
            setState { current -> current.copy(comments = current.comments + comment) }
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

    fun addReaction(
        targetId: String,
        reactionType: ReactionType,
        targetType: TargetType,
    ) {
        viewModelScope.launch {
            val result =
                addReactionUseCase(targetId, reactionType, targetType)
            if (result.isFailure) return@launch
            val reaction = result.getOrNull() ?: return@launch
            if (targetType == TargetType.POST) {
                val reactions = currentState.post?.reactions ?: return@launch
                setState { current ->
                    current.run {
                        copy(post = post?.copy(reactions = reactions + reaction))
                    }
                }
            }
        }
    }

    fun deleteReaction(reactionId: String) {
        viewModelScope.launch {
            val result = deleteReactionUseCase(reactionId)
            if (result.isFailure) return@launch
            val reactions =
                currentState.post
                    ?.reactions
                    ?.filterNot { it.id == reactionId }
                    .orEmpty()
            setState { current ->
                current.run {
                    copy(post = post?.copy(reactions = reactions))
                }
            }
        }
    }

    fun toggleReaction(
        targetId: String,
        targetType: TargetType,
        reactionType: ReactionType,
    ) {
        val postReaction = currentState.post?.reactions?.firstOrNull { it.targetId == targetId }
        if (postReaction != null) {
            deleteReaction(postReaction.id)
            return
        }
        addReaction(targetId, reactionType, targetType)
    }
}

internal data class PostDetailState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val selectedComment: Comment? = null,
)
