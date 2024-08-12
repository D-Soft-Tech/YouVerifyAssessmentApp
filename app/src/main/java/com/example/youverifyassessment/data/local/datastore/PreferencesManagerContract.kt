package com.example.youverifyassessment.data.local.datastore

import com.dsofttech.dprefs.enums.DPrefsDefaultValue
import kotlinx.coroutines.flow.Flow

interface PreferencesManagerContract {
    suspend fun retrieveStringFromSharedPrefs(
        key: String,
        defaultValue: String = DPrefsDefaultValue.DEFAULT_VALUE_STRING.value as String,
    ): Flow<String?>

    suspend fun retrieveIntFromSharedPrefs(key: String, defaultValue: Int? = null): Flow<Int?>
    suspend fun saveStringToSharedPrefs(key: String, value: String)
    suspend fun saveIntToSharedPrefs(key: String, value: Int)
    suspend fun saveBooleanToSharedPrefs(key: String, value: Boolean)
    suspend fun saveLongToSharedPrefs(key: String, value: Long)
    suspend fun saveDoubleToSharedPrefs(key: String, value: Double)
    suspend fun retrieveBooleanFromSharedPrefs(key: String, defaultValue: Boolean = false): Flow<Boolean>
    suspend fun retrieveLongFromSharedPrefs(key: String, defaultValue: Long? = null): Flow<Long?>
    suspend fun retrieveDoubleFromSharedPrefs(key: String, defaultValue: Double? = null): Flow<Double?>
}