package com.widi.storyapp.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_pref")

class StoryReferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val language = stringPreferencesKey("en")
    private val token = stringPreferencesKey("token")

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[this.token] = token
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[token]
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(token)
        }
    }

    suspend fun saveLanguageSetting(language: String) {
        dataStore.edit { preferences ->
            preferences[this.language] = language
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryReferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): StoryReferences {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryReferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}