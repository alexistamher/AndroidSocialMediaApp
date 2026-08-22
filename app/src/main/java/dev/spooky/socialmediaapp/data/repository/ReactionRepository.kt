@file:Suppress("ktlint:standard:filename")

package dev.spooky.socialmediaapp.data.repository

import dev.spooky.socialmediaapp.core.util.failed
import dev.spooky.socialmediaapp.data.dto.AddReactionRequest
import dev.spooky.socialmediaapp.data.dto.UpdateReactionRequest
import dev.spooky.socialmediaapp.data.dto.toDomain
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.domain.models.Reaction
import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.repository.ReactionRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import dev.spooky.socialmediaapp.data.dto.Reaction as DataReaction

class ReactionRepositoryImpl(
    private val http: HttpClient,
    private val baseUrl: String,
    private val sessionHelper: SessionHelper,
) : ReactionRepository {
    override suspend fun addReaction(
        targetId: String,
        reactionType: String,
        targetType: String,
    ): Result<Reaction> {
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val reqBody = AddReactionRequest(targetId, reactionType, targetType)
        val response =
            http.request("$baseUrl/reactions") {
                method = HttpMethod.Post
                bearerAuth(auth.accessToken)
                setBody(reqBody)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on adding reaction")
        }
        val body = response.body<DataReaction>()
        return Result.success(body.toDomain())
    }

    override suspend fun deleteReaction(reactionId: String): Result<Unit> {
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val response =
            http.request("$baseUrl/reactions/$reactionId") {
                method = HttpMethod.Delete
                bearerAuth(auth.accessToken)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on deleting reaction")
        }

        return Result.success(Unit)
    }

    override suspend fun updateReaction(
        reactionId: String,
        reactionType: ReactionType,
    ): Result<Unit> {
        val reqBody = UpdateReactionRequest(reactionId, reactionType.description)
        val auth = sessionHelper.getAuth() ?: return Result.failed("unauthorized")
        val response =
            http.request("$baseUrl/reactions") {
                method = HttpMethod.Put
                bearerAuth(auth.accessToken)
                setBody(reqBody)
            }
        if (response.status != HttpStatusCode.OK) {
            return Result.failed("failed on updating reaction")
        }

        return Result.success(Unit)
    }
}
