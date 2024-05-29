package com.capstone.dressify.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.dressify.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreference constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveUserToken(user: User) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.username
            preferences[TOKEN_KEY] = user.token
            preferences[LOG_KEY] = user.isLoggedIn
            preferences[EMAIL_KEY] = user.email
        }
    }

    fun getToken(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[LOG_KEY] ?: false
            )
        }
    }



    suspend fun isLogin(){
        dataStore.edit { preferences ->
            preferences[LOG_KEY] = true
        }
    }

    suspend fun clearUserToken() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val LOG_KEY = booleanPreferencesKey("log")
        private val EMAIL_KEY = stringPreferencesKey("email")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}