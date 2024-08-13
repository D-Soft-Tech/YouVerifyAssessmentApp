package com.example.youverifyassessment.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youverifyassessment.data.local.datastore.sessions.SessionManagerContract
import com.example.youverifyassessment.domain.model.UserDetailsDomain
import com.example.youverifyassessment.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AppViewModel @Inject constructor(
    private val sessionManagerContract: SessionManagerContract,
    @Named(AppConstants.IO_DISPATCHER_DI_NAME) private val ioDispatcher: CoroutineContext
) : ViewModel() {

    fun saveUser(account: UserDetailsDomain) {
        viewModelScope.launch(ioDispatcher) {
            sessionManagerContract.saveUser(account)
        }
    }
}