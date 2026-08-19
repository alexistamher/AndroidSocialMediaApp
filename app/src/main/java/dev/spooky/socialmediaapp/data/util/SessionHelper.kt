package dev.spooky.socialmediaapp.data.util

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
    private var authData: AuthData? = null

    private val isAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun validateSession(): Boolean {
        if (authData != null && _userInfo != null) return true
        val data = repository.getAuthDataPreferences() ?: return false
        authData = data
        isAuthorized.update { true }
        val info = repository.getUserInfoPreferences() ?: return false
        _userInfo = info
        return true
    }

    suspend fun getUserInfo(): DomainUserInfo? {
        if (!isAuthorized.value) return null
        if (_userInfo == null) {
            val info = repository.getUserInfoPreferences() ?: return null
            _userInfo = info
            isAuthorized.update { true }
        }
        return _userInfo?.run { DomainUserInfo(id, displayName, email, photo) }
    }

    suspend fun getAuth(): DomainAuthData? {
        if (authData != null) {
            return authData?.toDomain()
        }
        return repository
            .getAuthDataPreferences()
            ?.toDomain()
    }

    suspend fun setAuth(data: AuthData) {
        authData = data
        isAuthorized.update { true }
        repository.setAuthPreferences(data)
    }

    suspend fun setUserInfo(info: UserInfo) {
        repository.setUserInfoPreferences(info)
    }
}
