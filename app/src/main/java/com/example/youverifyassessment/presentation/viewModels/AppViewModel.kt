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
    private val sessionManagerUseCase: SessionManagerContract,
    private val productsUseCase: GetProductsContract,
    private val shoppingCartUseCase: ShoppingCartContract,
    @Named(AppConstants.IO_DISPATCHER_DI_NAME) private val ioDispatcher: CoroutineContext
) : ViewModel() {
    private val _currentlyLoggedInUser: MutableStateFlow<UserDetailsDomain?> =
        MutableStateFlow(null)
    val currentlyLoggedInUser: StateFlow<UserDetailsDomain?> get() = _currentlyLoggedInUser
    val currentlyLoggedInUserLiveData: LiveData<UserDetailsDomain?> get() = _currentlyLoggedInUser.asLiveData()

    val products = productsUseCase.getProducts(0).flow.cachedIn(viewModelScope)

    private val _shoppingCart: MutableStateFlow<List<ShoppingItemDomain>> =
        MutableStateFlow(emptyList())
    val shoppingCart: LiveData<List<ShoppingItemDomain>> get() = _shoppingCart.asLiveData()

    private val _totalItemsInCart: MutableStateFlow<String> = MutableStateFlow("0")
    val totalItemsInCart: LiveData<String> get() = _totalItemsInCart.asLiveData()

    fun saveUser(account: UserDetailsDomain) {
        viewModelScope.launch(ioDispatcher) {
            sessionManagerUseCase.saveUser(account)
        }
    }

    fun getCurrentlyLoggedInUser() {
        viewModelScope.launch(ioDispatcher) {
            sessionManagerUseCase.getCurrentlyLoggedInUser().collect {
                _currentlyLoggedInUser.value = it
            }
        }
    }

    fun insertUpdateOrRemoveShoppingItem(shoppingItem: ShoppingItemDomain, isIncrease: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartUseCase.insertUpdateOrRemoveShoppingItem(shoppingItem, isIncrease)
        }
    }

    fun getTotalItemsInShoppingCart() {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartUseCase.getTotalItemsInShoppingCart().collect { total ->
                total.let {
                    _totalItemsInCart.value = it.toString()
                }
            }
        }
    }

    fun getShoppingCart() {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartUseCase.fetchShoppingItems().collect {
                _shoppingCart.value = it
            }
        }
    }
}