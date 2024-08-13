package com.example.youverifyassessment.domain.usecases

import com.example.youverifyassessment.data.local.db.dao.ShoppingCartDao
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.domain.repository.ShoppingCartContract
import com.example.youverifyassessment.utils.ModelMapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartUseCase @Inject constructor(
    private val shoppingCartDao: ShoppingCartDao
) : ShoppingCartContract {
    override suspend fun updateShoppingItemQuantityOrRemove(shoppingItem: ShoppingItemDomain): Int =
        if (shoppingItem.quantity.toInt() == 0) {
            shoppingCartDao.deleteShoppingItem(shoppingItem.shoppingCartId.toInt())
        } else {
            shoppingCartDao.insertShoppingItem(shoppingItem)
        }

    override fun fetchShoppingItems(): Flow<List<ShoppingItemDomain>> =
        shoppingCartDao.getShoppingCart().map { shoppingItemData ->
            shoppingItemData.map { it.toDomain() }
        }
}