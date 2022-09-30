package com.rijaldev.history.data

import com.rijaldev.history.data.local.datastore.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserPreference : UserPreference {

    private var loginStatus = false
    private var token: String? = null

    override fun isLoggedIn(): Flow<Boolean> = flow {
        emit(loginStatus)
    }

    override suspend fun setLoginStatus(isLogin: Boolean) {
        loginStatus = isLogin
    }

    override fun getToken(): Flow<String?> = flow {
        emit(token)
    }

    override suspend fun saveToken(token: String) {
        this.token = token
    }

    override suspend fun deleteToken() {
        this.token = null
    }
}