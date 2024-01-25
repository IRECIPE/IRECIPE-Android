package com.umcproject.irecipe.data.remote.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.umcproject.irecipe.domain.model.User
import com.umcproject.irecipe.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.IllegalStateException
import javax.inject.Inject

class UserDataRepositoryIml @Inject constructor(
    private val context: Context
): UserDataRepository {
    val Context.dataStore by preferencesDataStore(name = "user_data")
    private val _userData = MutableStateFlow(User())

    companion object{
        private val NUM_KEY = stringPreferencesKey("num")
    }

    override suspend fun getUserData(): User {
        val userData = context.dataStore.data
            .catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }.map { preference ->
                mapperToUserData(preference)
            }.first()
        return userData
    }

    override suspend fun setUserData(key: String, value: String) {
        context.dataStore.edit { preferences ->
            val preferKey = when(key){
                "num" -> {
                    _userData.value.num = value
                    NUM_KEY
                }
                else -> throw IllegalStateException("Unknown key: $key")
            }
            preferences[preferKey] = value
        }
    }

    fun mapperToUserData(preferences: Preferences): User{
        val num = preferences[NUM_KEY] ?: ""

        return User(num)
    }

}