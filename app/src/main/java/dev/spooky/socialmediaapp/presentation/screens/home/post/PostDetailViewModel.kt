package dev.spooky.socialmediaapp.presentation.screens.home.post

import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.models.Post
import dev.spooky.socialmediaapp.domain.models.PreviewReaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.TargetType
import dev.spooky.socialmediaapp.domain.usecase.home.AddCommentUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetCommentsByTargetIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostByIdUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.ToggleReactionUseCase
import dev.spooky.socialmediaapp.presentation.screens.BaseViewModel
import kotlinx.coroutines.launch

internal class PostDetailViewModel(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getCommentsByTargetIdUseCase: GetCommentsByTargetIdUseCase,
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
                getComments(post.id)
            }
    }

    fun getComments(targetId: String) {
        viewModelScope.launch {
            val result = getCommentsByTargetIdUseCase(targetId)
            if (result.isFailure) return@launch
            val comments = result.getOrNull() ?: return@launch
            setState { current -> current.copy(comments = comments) }
        }
    }

    fun getCommentComments(commentId: String) {
        viewModelScope.launch {
            val result = getCommentsByTargetIdUseCase(commentId, TargetType.COMMENT)
            if (result.isFailure) return@launch
            val commentChildren = result.getOrNull() ?: return@launch
            val commentIdx =
                currentState.comments.indexOfFirst { it.id == commentId }.takeIf { it != -1 }
                    ?: return@launch
            val comment = currentState.comments[commentIdx].copy(commentChildren = commentChildren)
            val newComments =
                currentState.comments.toMutableList().apply {
                    set(commentIdx, comment)
                }
            setState { current -> current.copy(comments = newComments) }
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

    fun findComment(
        comment: Comment,
        commentId: String,
        prevLevel: Int,
    ): Pair<Comment, Int>? {
        if (comment.id == commentId) return Pair(comment, prevLevel)
        val tempComment = comment.commentChildren?.firstOrNull { it.id == commentId }
        if (tempComment != null) return Pair(tempComment, prevLevel + 1)
        return comment.commentChildren
            ?.firstOrNull {
                findComment(
                    it,
                    commentId,
                    prevLevel + 1,
                ) != null
            }?.let { Pair(it, prevLevel) }
    }

    fun insertNestedReactions(
        commentId: String,
        reactions: List<PreviewReaction>,
        comments: List<Comment>?,
    ): List<Comment>? =
        comments?.map {
            if (it.id == commentId) {
                it.copy(previewReactions = reactions)
            } else {
                it.copy(
                    commentChildren =
                        insertNestedReactions(
                            commentId,
                            reactions,
                            it.commentChildren,
                        ),
                )
            }
        }

    fun toggleReaction(
        targetId: String,
        targetType: TargetType,
        reactionType: ReactionType,
    ) {
        if (targetType == TargetType.COMMENT) {
            val comments = currentState.comments
            val comment = comments.firstNotNullOf { findComment(it, targetId, 0) }
            viewModelScope.launch {
                val result =
                    toggleReactionUseCase(
                        targetId,
                        targetType,
                        reactionType,
                        comment.first.previewReactions,
                    )
                if (result.isFailure) return@launch
                val newReactions = result.getOrNull() ?: return@launch
                val newComments = insertNestedReactions(targetId, newReactions, comments).orEmpty()
                setState { currentState.copy(comments = newComments) }
            }
            return
        }

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
