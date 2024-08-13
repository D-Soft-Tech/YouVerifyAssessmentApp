package com.example.youverifyassessment.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.youverifyassessment.data.local.datastore.sessions.SessionManagerContract
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.domain.model.UserDetailsDomain
import com.example.youverifyassessment.domain.repository.GetProductsContract
import com.example.youverifyassessment.domain.repository.ShoppingCartContract
import com.example.youverifyassessment.presentation.viewStates.ViewState
import com.example.youverifyassessment.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AppViewModel @Inject constructor(
    private val sessionManagerContract: SessionManagerContract,
    private val getProductsContract: GetProductsContract,
    private val shoppingCartContract: ShoppingCartContract,
    @Named(AppConstants.IO_DISPATCHER_DI_NAME) private val ioDispatcher: CoroutineContext
) : ViewModel() {
    private val _currentlyLoggedInUser: MutableStateFlow<ViewState<UserDetailsDomain>> =
        MutableStateFlow(ViewState.initialDefault(null))
    val currentlyLoggedInUser: StateFlow<ViewState<UserDetailsDomain>> get() = _currentlyLoggedInUser
    val currentlyLoggedInUserLiveData: LiveData<ViewState<UserDetailsDomain>> get() = _currentlyLoggedInUser.asLiveData()

    val products = getProductsContract.getProducts(0).flow.cachedIn(viewModelScope)

    private val _shoppingCart: MutableStateFlow<List<ShoppingItemDomain>> =
        MutableStateFlow(emptyList())
    val shoppingCart: StateFlow<List<ShoppingItemDomain>> get() = _shoppingCart

    fun saveUser(account: UserDetailsDomain) {
        viewModelScope.launch(ioDispatcher) {
            sessionManagerContract.saveUser(account)
        }
    }

    fun getCurrentlyLoggedInUser() {
        viewModelScope.launch(ioDispatcher) {
            sessionManagerContract.getCurrentlyLoggedInUser().collect {
                _currentlyLoggedInUser.value =
                    it?.let { user -> ViewState.success(user) } ?: ViewState.error(
                        null,
                        "No current user, please go to Login screen to login"
                    )
            }
        }
    }

    fun insertUpdateOrRemoveShoppingItem(shoppingItem: ShoppingItemDomain) {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartContract.insertUpdateOrRemoveShoppingItem(shoppingItem)
        }
    }

    fun getShoppingCart() {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartContract.fetchShoppingItems().collect {
                _shoppingCart.value = it
            }
        }
    }
}