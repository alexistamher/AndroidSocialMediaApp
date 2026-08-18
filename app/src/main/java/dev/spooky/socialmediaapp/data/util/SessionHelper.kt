package dev.spooky.socialmediaapp.data.util

import androidx.annotation.VisibleForTesting
import dev.spooky.socialmediaapp.data.models.AuthData
import dev.spooky.socialmediaapp.data.models.UserInfo
import dev.spooky.socialmediaapp.data.models.toDomain
import dev.spooky.socialmediaapp.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import dev.spooky.socialmediaapp.domain.models.AuthData as DomainAuthData
import dev.spooky.socialmediaapp.domain.models.UserInfo as DomainUserInfo

class SessionHelper(
    private val repository: SessionRepository,
) {
    private var _userInfo: UserInfo? = null
    private var _authData: AuthData? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val _isAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun isAuthorized(): Boolean = _isAuthorized.value

    suspend fun getUserInfo(): DomainUserInfo? {
        if (!_isAuthorized.value) return null
        if (_userInfo == null) {
            val info = repository.getUserInfoPreferences() ?: return null
            _userInfo = info
            _isAuthorized.update { true }
        }
        return _userInfo?.run { DomainUserInfo(id, displayName, email, photo) }
    }

    suspend fun getAuth(): DomainAuthData? {
        if (_authData != null) {
            return _authData?.toDomain()
        }
        return repository
            .getAuthDataPreferences()
            ?.toDomain()
    }

    suspend fun setAuth(data: AuthData) {
        _authData = data
        _isAuthorized.update { true }
        repository.setAuthPreferences(data)
    }

    suspend fun setUserInfo(info: UserInfo) {
        repository.setUserInfoPreferences(info)
    }
}
