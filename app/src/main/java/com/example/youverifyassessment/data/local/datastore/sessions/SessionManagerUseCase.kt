package com.example.youverifyassessment.data.local.datastore.sessions

import android.util.Log
import com.example.youverifyassessment.data.local.datastore.PreferencesManagerContract
import com.example.youverifyassessment.domain.model.UserDetailsDomain
import com.example.youverifyassessment.utils.AppConstants.USER_ACCOUNT_PREF_TAG
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerUseCase @Inject constructor(
    private val preferencesManager: PreferencesManagerContract,
    private val gson: Gson
) : SessionManagerContract {
    override suspend fun saveUser(user: UserDetailsDomain) {
        val userAsString = gson.toJson(user)
        Log.d("LOGGED_IN_USER", userAsString)
        preferencesManager.saveStringToSharedPrefs(USER_ACCOUNT_PREF_TAG, userAsString)
    }

    override fun getCurrentlyLoggedInUser(): Flow<UserDetailsDomain?> =
        preferencesManager.retrieveStringFromSharedPrefs(USER_ACCOUNT_PREF_TAG).map {
            it?.let {
                gson.fromJson(it, UserDetailsDomain::class.java)
            }
        }
}