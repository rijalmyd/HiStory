package com.rijaldev.history.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rijaldev.history.utils.Constant.LOGIN_STATUS_KEY
import com.rijaldev.history.utils.Constant.TOKEN_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreference {

    override fun isLoggedIn(): Flow<Boolean> =
        dataStore.data.map {
            it[LOGIN_STATUS_KEY] ?: false
        }

    override suspend fun setLoginStatus(isLogin: Boolean) {
        dataStore.edit {
            it[LOGIN_STATUS_KEY] = isLogin
        }
    }

    override fun getToken(): Flow<String?> =
        dataStore.data.map {
            it[TOKEN_KEY]
        }

    override suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    override suspend fun deleteToken() {
        dataStore.edit {
            it.remove(TOKEN_KEY)
        }
    }
}