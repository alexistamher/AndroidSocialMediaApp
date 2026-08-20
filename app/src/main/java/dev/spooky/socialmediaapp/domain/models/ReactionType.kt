package dev.spooky.socialmediaapp.domain.models

enum class ReactionType(
    val description: String,
) {
    LIKE("like"),
    LOVE("love"),
    HAHA("haha"),
    WOW("wow"),
    SAD("sad"),
    ANGRY("angry"),
    ;

    companion object
}

fun ReactionType.Companion.fromString(value: String): ReactionType =
    when (value) {
        "love" -> ReactionType.LOVE
        "haha" -> ReactionType.HAHA
        "wow" -> ReactionType.WOW
        "sad" -> ReactionType.SAD
        "angry" -> ReactionType.ANGRY
        else -> ReactionType.LIKE
    }
