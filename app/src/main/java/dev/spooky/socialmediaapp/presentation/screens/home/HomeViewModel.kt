package dev.spooky.socialmediaapp.presentation.screens.home

import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.core.util.error
import dev.spooky.socialmediaapp.domain.models.PostPreview
import dev.spooky.socialmediaapp.domain.usecase.auth.LogoutUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.AddPostUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.DeletePostUseCase
import dev.spooky.socialmediaapp.domain.usecase.home.GetPostsUseCase
import dev.spooky.socialmediaapp.presentation.screens.BaseViewModel
import dev.spooky.socialmediaapp.presentation.util.ScreenState
import dev.spooky.socialmediaapp.presentation.util.asSuccess
import dev.spooky.socialmediaapp.presentation.util.isSuccess
import dev.spooky.socialmediaapp.presentation.util.success
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val getPostsUseCase: GetPostsUseCase,
    private val addPostUseCase: AddPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<ScreenState<HomeState>>() {
    override fun initialState() = ScreenState.Idle

    lateinit var onLogout: () -> Unit

    fun getPosts() {
        setState { ScreenState.Loading() }
        viewModelScope.launch {
            val result = getPostsUseCase()
            if (result.isFailure) {
                setState { ScreenState.Error(result.error()) }
                return@launch
            }
            val posts = result.getOrNull() ?: return@launch
            setState { ScreenState.Success(HomeState(posts = posts)) }
        }
    }

    fun addPost(content: String) {
        viewModelScope.launch {
            val result = addPostUseCase(content)
            if (result.isFailure) {
                setState { ScreenState.Error(result.error()) }
                return@launch
            }
            val post = result.getOrNull() ?: return@launch
            if (currentState.isSuccess()) {
                val current =
                    currentState.success().run {
                        copy(posts = posts + post)
                    }
                currentState.asSuccess(current)
                setState { ScreenState.Success(current) }
                return@launch
            }
            setState { ScreenState.Success(HomeState(posts = listOf(post))) }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            val result = deletePostUseCase(postId)
            if (result.isFailure) {
                return@launch
            }
            val newPosts = currentState.success().posts.filterNot { it.id == postId }
            setState { current ->
                current.asSuccess().run {
                    copy(data = data.copy(posts = newPosts))
                }
            }
        }
    }

    fun selectPost(postId: String?) {
        setState { current ->
            current.asSuccess().run {
                copy(data = data.copy(postId = postId))
            }
        }
    }

    fun logout() {
        viewModelScope
            .launch {
                logoutUseCase()
            }.invokeOnCompletion {
                if (!::onLogout.isInitialized) return@invokeOnCompletion
                onLogout()
            }
    }
}

internal data class HomeState(
    val posts: List<PostPreview> = emptyList(),
    val postId: String? = null,
)
