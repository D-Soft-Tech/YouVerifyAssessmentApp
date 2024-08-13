package com.example.youverifyassessment.data.local.datastore

import com.dsofttech.dprefs.enums.DPrefsDefaultValue
import kotlinx.coroutines.flow.Flow

interface PreferencesManagerContract {
    fun retrieveStringFromSharedPrefs(
        key: String,
        defaultValue: String = DPrefsDefaultValue.DEFAULT_VALUE_STRING.value as String,
    ): Flow<String?>

    fun retrieveIntFromSharedPrefs(key: String, defaultValue: Int? = null): Flow<Int?>
    suspend fun saveStringToSharedPrefs(key: String, value: String)
    suspend fun saveIntToSharedPrefs(key: String, value: Int)
    suspend fun saveBooleanToSharedPrefs(key: String, value: Boolean)
    suspend fun saveLongToSharedPrefs(key: String, value: Long)
    suspend fun saveDoubleToSharedPrefs(key: String, value: Double)
    fun retrieveBooleanFromSharedPrefs(key: String, defaultValue: Boolean = false): Flow<Boolean>
    fun retrieveLongFromSharedPrefs(key: String, defaultValue: Long? = null): Flow<Long?>
    fun retrieveDoubleFromSharedPrefs(key: String, defaultValue: Double? = null): Flow<Double?>
}