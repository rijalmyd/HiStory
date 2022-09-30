package com.rijaldev.history.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constant {
    const val USER_PREFERENCE = "user_preference"
    val LOGIN_STATUS_KEY = booleanPreferencesKey("login")
    val TOKEN_KEY = stringPreferencesKey("token")
}