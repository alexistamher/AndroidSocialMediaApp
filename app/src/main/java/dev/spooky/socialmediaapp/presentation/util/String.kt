package dev.spooky.socialmediaapp.presentation.util

internal fun isEmailValid(value: String): Boolean =
    android.util.Patterns.EMAIL_ADDRESS
        .matcher(value)
        .matches()

internal fun isPasswordValid(value: String): Boolean {
    val pwdRegex = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$""".toRegex()
    return pwdRegex.matches(value)
}
