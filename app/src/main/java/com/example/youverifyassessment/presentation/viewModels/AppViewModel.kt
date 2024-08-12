package com.example.youverifyassessment.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youverifyassessment.data.local.datastore.PreferencesManagerContract
import com.example.youverifyassessment.utils.AppConstants
import com.example.youverifyassessment.utils.AppConstants.USER_ACCOUNT_PREF_TAG
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AppViewModel @Inject constructor(
    private val prefsUseCase: PreferencesManagerContract,
    private val gson: Gson,
    @Named(AppConstants.IO_DISPATCHER_DI_NAME) private val ioDispatcher: CoroutineContext
) : ViewModel() {

    fun saveUser(account: GoogleSignInAccount) {
        viewModelScope.launch(ioDispatcher) {
            val data = gson.toJson(account)
            prefsUseCase.saveStringToSharedPrefs(USER_ACCOUNT_PREF_TAG, data)
        }
    }
}