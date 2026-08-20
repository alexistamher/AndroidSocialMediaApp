package dev.spooky.socialmediaapp.data.dto

import dev.spooky.socialmediaapp.domain.models.Comment as DomainComment
import dev.spooky.socialmediaapp.domain.models.Post as DomainPost
import dev.spooky.socialmediaapp.domain.models.Reaction as DomainReaction

internal fun Post.toDomain(): DomainPost =
    DomainPost(
        id = id,
        content = content,
        author = author.toDomain(),
        reactions = reactions.map { it.toDomain() },
        visibility = visibility,
        createdAt = createdAt,
    )

internal fun Reaction.toDomain(): DomainReaction =
    DomainReaction(
        id = id,
        targetId = targetId,
        reactionType = reactionType,
        createdAt = createdAt,
        author = author.toDomain(),
    )

internal fun Comment.toDomain(): DomainComment =
    DomainComment(
        id = id,
        content = content,
        author = author.toDomain(),
        previewReactions = previewReactions,
        createdAt = createdAt,
        postId = postId,
        parentCommentId = parentCommentId,
    )
