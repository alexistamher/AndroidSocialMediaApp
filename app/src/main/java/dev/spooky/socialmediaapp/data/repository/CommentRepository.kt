@file:Suppress("ktlint:standard:filename")

package dev.spooky.socialmediaapp.data.repository

import dev.spooky.socialmediaapp.core.util.failed
import dev.spooky.socialmediaapp.data.dto.AddCommentRequest
import dev.spooky.socialmediaapp.data.dto.GetCommentsResponse
import dev.spooky.socialmediaapp.data.dto.toDomain
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.domain.models.Comment
import dev.spooky.socialmediaapp.domain.repository.CommentRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import dev.spooky.socialmediaapp.data.dto.Comment as DataComment

class CommentRepositoryImpl(
    private val http: HttpClient,
    private val baseUrl: String,
    private val sessionHelper: SessionHelper,
) : CommentRepository {
    override suspend fun getCommentsByPostId(postId: String): Result<List<Comment>> {
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val response =
            http.request("$baseUrl/comments/post/$postId") {
                method = HttpMethod.Get
                bearerAuth(auth.accessToken)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on getting post comments")
        }
        val res = response.body<GetCommentsResponse>()
        val comments = res.comments.map { it.toDomain() }
        return Result.success(comments)
    }

    override suspend fun addComment(
        content: String,
        postId: String,
        parentCommentId: String?,
    ): Result<Comment> {
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val reqBody = AddCommentRequest(content, postId, parentCommentId)
        val response =
            http.request("$baseUrl/comments") {
                method = HttpMethod.Post
                bearerAuth(auth.accessToken)
                setBody(reqBody)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on getting post comments")
        }
        val res = response.body<DataComment>()
        val comment = res.toDomain()
        return Result.success(comment)
    }
}
