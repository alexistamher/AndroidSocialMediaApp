package dev.spooky.socialmediaapp.data.dto

import dev.spooky.socialmediaapp.domain.models.ReactionType
import dev.spooky.socialmediaapp.domain.models.fromString
import dev.spooky.socialmediaapp.domain.models.Author as DomainAuthor
import dev.spooky.socialmediaapp.domain.models.Comment as DomainComment
import dev.spooky.socialmediaapp.domain.models.Post as DomainPost
import dev.spooky.socialmediaapp.domain.models.PostPreview as DomainPostPreview
import dev.spooky.socialmediaapp.domain.models.PreviewReaction as DomainPreviewReaction
import dev.spooky.socialmediaapp.domain.models.Reaction as DomainReaction

fun Author.toDomain(): DomainAuthor = DomainAuthor(id, username, displayName, avatarURL = null)

fun PostPreview.toDomain(): DomainPostPreview =
    DomainPostPreview(
        id = id,
        content = content,
        author = author.toDomain(),
        commentsCount = commentsCount,
        previewReactions = previewReactions.map { it.toDomain() },
        visibility = visibility,
        createdAt = createdAt,
    )

internal fun Post.toDomain(): DomainPost =
    DomainPost(
        id = id,
        content = content,
        author = author.toDomain(),
        previewReactions = previewReactions.map { it.toDomain() },
        visibility = visibility,
        createdAt = createdAt,
    )

internal fun Reaction.toDomain(): DomainReaction =
    DomainReaction(
        id = id,
        targetId = targetId,
        reactionType = ReactionType.fromString(reactionType),
        createdAt = createdAt,
        author = author.toDomain(),
    )

internal fun Comment.toDomain(): DomainComment =
    DomainComment(
        id = id,
        content = content,
        author = author.toDomain(),
        previewReactions = previewReactions.map { it.toDomain() },
        createdAt = createdAt,
        postId = postId,
        parentCommentId = parentCommentId,
    )

internal fun PreviewReaction.toDomain(): DomainPreviewReaction =
    DomainPreviewReaction(
        id,
        reactionType,
        targetId,
        authorId,
    )
