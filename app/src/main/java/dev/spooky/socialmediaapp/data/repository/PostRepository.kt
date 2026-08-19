package dev.spooky.socialmediaapp.data.repository

import dev.spooky.socialmediaapp.core.util.failed
import dev.spooky.socialmediaapp.data.dto.AddPostRequest
import dev.spooky.socialmediaapp.data.dto.GetPostsResponse
import dev.spooky.socialmediaapp.data.dto.toDomain
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.domain.models.PostPreview
import dev.spooky.socialmediaapp.domain.repository.PostRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import dev.spooky.socialmediaapp.data.dto.PostPreview as DataPostPreview

class PostRepositoryImpl(
    private val http: HttpClient,
    private val baseUrl: String,
    private val sessionHelper: SessionHelper,
) : PostRepository {
    override suspend fun getPostsByUserId(): Result<List<PostPreview>> {
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val response =
            http.request("$baseUrl/posts") {
                method = HttpMethod.Get
                bearerAuth(auth.accessToken)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on getting posts")
        }
        val res = response.body<GetPostsResponse>()
        val posts = res.posts.map { it.toDomain() }
        return Result.success(posts)
    }

    override suspend fun addPost(
        content: String,
        visibility: String,
    ): Result<PostPreview> {
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val body = AddPostRequest(content = content, visibility = visibility, null)
        val response =
            http.request("$baseUrl/posts") {
                method = HttpMethod.Post
                bearerAuth(auth.accessToken)
                setBody(body)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on adding post")
        }
        val res = response.body<DataPostPreview>()
        return Result.success(res.toDomain())
    }

    override suspend fun deletePost(postId: String): Result<Unit> {
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val response =
            http.request("$baseUrl/posts/$postId") {
                method = HttpMethod.Delete
                bearerAuth(auth.accessToken)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on getting posts")
        }
        return Result.success(Unit)
    }
}
