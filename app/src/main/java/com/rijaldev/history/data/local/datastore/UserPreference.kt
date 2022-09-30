package com.rijaldev.history.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface UserPreference {
    fun isLoggedIn(): Flow<Boolean>

    suspend fun setLoginStatus(isLogin: Boolean)

    fun getToken(): Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun deleteToken()
}