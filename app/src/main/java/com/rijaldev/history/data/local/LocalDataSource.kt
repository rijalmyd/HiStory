package com.rijaldev.history.data.local

import com.rijaldev.history.data.local.datastore.UserPreferenceImpl
import com.rijaldev.history.data.local.room.StoryDatabase
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val userPref: UserPreferenceImpl,
    private val storyDatabase: StoryDatabase
) {
    fun isLoggedIn() = userPref.isLoggedIn()

    suspend fun setLoginStatus(isLogin: Boolean) = userPref.setLoginStatus(isLogin)

    fun getToken() = userPref.getToken()

    suspend fun saveToken(token: String) = userPref.saveToken(token)

    suspend fun deleteToken() = userPref.deleteToken()

    fun getDatabase() = storyDatabase
}