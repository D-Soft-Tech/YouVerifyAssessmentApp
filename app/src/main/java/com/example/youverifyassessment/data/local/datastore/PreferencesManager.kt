package com.example.youverifyassessment.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.youverifyassessment.domain.encryption.DataEncryptionContract
import com.example.youverifyassessment.utils.AppConstants.PREFS_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptionContract: DataEncryptionContract
) : PreferencesManagerContract {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFS_NAME)
    private val dataStore = context.dataStore

    private fun createStringKey(key: String): Preferences.Key<String> = stringPreferencesKey(key)
    private fun createIntKey(key: String): Preferences.Key<Int> = intPreferencesKey(key)
    private fun createLongKey(key: String): Preferences.Key<Long> = longPreferencesKey(key)
    private fun createBooleanKey(key: String): Preferences.Key<Boolean> = booleanPreferencesKey(key)
    private fun createDoubleKey(key: String): Preferences.Key<Double> = doublePreferencesKey(key)

    override fun retrieveStringFromSharedPrefs(key: String, defaultValue: String): Flow<String?> {
        val prefKey = createStringKey(key)
        return dataStore.data.map {
            it[prefKey] ?: defaultValue
        }.catch {
            emit(defaultValue)
        }
    }

    override fun retrieveIntFromSharedPrefs(key: String, defaultValue: Int?): Flow<Int?> {
        val prefKey = createIntKey(key)
        return dataStore.data.
            map {
                it[prefKey] ?: defaultValue
            }
            .catch {
            emit(defaultValue)
        }
    }

    override suspend fun saveStringToSharedPrefs(key: String, value: String) {
        val prefKey = createStringKey(key)
        val encryptedData = encryptionContract.encrypt(value)
        dataStore.edit {
            it[prefKey] = encryptedData
        }
    }

    override suspend fun saveIntToSharedPrefs(key: String, value: Int) {
        val prefKey = createIntKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    override suspend fun saveBooleanToSharedPrefs(key: String, value: Boolean) {
        val prefKey = createBooleanKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    override suspend fun saveLongToSharedPrefs(key: String, value: Long) {
        val prefKey = createLongKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    override suspend fun saveDoubleToSharedPrefs(key: String, value: Double) {
        val prefKey = createDoubleKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    override fun retrieveBooleanFromSharedPrefs(key: String, defaultValue: Boolean): Flow<Boolean> {
        val prefKey = createBooleanKey(key)
        return dataStore.data.map {
            it[prefKey] ?: defaultValue
        }.catch {
            emit(defaultValue)
        }
    }

    override fun retrieveLongFromSharedPrefs(key: String, defaultValue: Long?): Flow<Long?> {
        val prefKey = createLongKey(key)
        return dataStore.data.
        map {
            it[prefKey] ?: defaultValue
        }.catch {
            emit(defaultValue)
        }
    }

    override fun retrieveDoubleFromSharedPrefs(key: String, defaultValue: Double?): Flow<Double?> {
        val prefKey = createDoubleKey(key)
        return dataStore.data.map {
            it[prefKey] ?: defaultValue
        }.catch {
            emit(defaultValue)
        }
    }
}