package dev.spooky.socialmediaapp.data.util

import dev.spooky.socialmediaapp.data.models.AuthData
import dev.spooky.socialmediaapp.data.models.UserInfo
import dev.spooky.socialmediaapp.data.models.toDomain
import dev.spooky.socialmediaapp.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import dev.spooky.socialmediaapp.domain.models.AuthData as DomainAuthData
import dev.spooky.socialmediaapp.domain.models.UserInfo as DomainUserInfo

class SessionHelper(
    private val repository: SessionRepository,
) {
    private var _userInfo: MutableStateFlow<DomainUserInfo?> = MutableStateFlow(null)
    val userInfo: StateFlow<DomainUserInfo?>
        get() = _userInfo
    private var _authData: MutableStateFlow<DomainAuthData?> = MutableStateFlow(null)
    val authData: StateFlow<DomainAuthData?>
        get() = _authData

    private val isAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun validateSession(): Boolean {
        if (authData.value != null && _userInfo.value != null) return true
        val data = repository.getAuthDataPreferences() ?: return false
        _authData.update { data.toDomain() }
        isAuthorized.update { true }
        val info = repository.getUserInfoPreferences() ?: return false
        _userInfo.update { info.toDomain() }
        return true
    }

    suspend fun getUserInfo(): DomainUserInfo? {
        if (!isAuthorized.value) return null
        if (_userInfo.value == null) {
            val info = repository.getUserInfoPreferences() ?: return null
            _userInfo.update { info.toDomain() }
            isAuthorized.update { true }
        }
        return _userInfo.value?.run { DomainUserInfo(id, displayName, email, photo) }
    }

    suspend fun getAuth(): DomainAuthData? {
        if (authData.value != null) {
            return authData.value
        }
        return repository
            .getAuthDataPreferences()
            ?.toDomain()
    }

    suspend fun setAuth(data: AuthData) {
        _authData.update { data.toDomain() }
        isAuthorized.update { true }
        repository.setAuthPreferences(data)
    }

    suspend fun setUserInfo(info: UserInfo) {
        repository.setUserInfoPreferences(info)
        _userInfo.update { info.toDomain() }
    }

    suspend fun reset() {
        repository.reset()
        _authData.update { null }
        _userInfo.update { null }
    }
}
