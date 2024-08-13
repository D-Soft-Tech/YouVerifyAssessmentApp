package com.example.youverifyassessment.domain.usecases

import androidx.room.withTransaction
import com.example.youverifyassessment.data.local.db.dao.ShoppingCartDao
import com.example.youverifyassessment.data.local.db.database.YouVerifyAppDatabase
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.domain.repository.ShoppingCartContract
import com.example.youverifyassessment.utils.ModelMapper.createFirstShoppingItemEntity
import com.example.youverifyassessment.utils.ModelMapper.createIncrementedOrDecrementedShoppingCartEntity
import com.example.youverifyassessment.utils.ModelMapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartUseCase @Inject constructor(
    private val database: YouVerifyAppDatabase,
    private val shoppingCartDao: ShoppingCartDao
) : ShoppingCartContract {
    override suspend fun insertUpdateOrRemoveShoppingItem(
        shoppingItem: ShoppingItemDomain,
        isIncrease: Boolean
    ): Int =
        when {
            shoppingItem.quantity.toInt() == 0 && isIncrease -> {
                val shoppingItemEntity = shoppingItem.createFirstShoppingItemEntity()
                shoppingCartDao.insertShoppingItem(shoppingItemEntity).toInt()
            }

            shoppingItem.quantity.toInt() == 0 && !isIncrease -> {
                shoppingCartDao.deleteShoppingItem(shoppingItem.product.id.toInt())
            }

            else -> {
                database.withTransaction {
                    val getIfShoppingItemExist =
                        shoppingCartDao.getShoppingItemByProductId(shoppingItem.product.id.toInt()) > 0
                    if (getIfShoppingItemExist) {
                        val shoppingItemEntity =
                            shoppingItem.createIncrementedOrDecrementedShoppingCartEntity(isIncrease)
                        shoppingCartDao.insertShoppingItem(shoppingItemEntity).toInt()
                    } else {
                        val shoppingItemEntity = shoppingItem.createFirstShoppingItemEntity()
                        shoppingCartDao.insertShoppingItem(shoppingItemEntity).toInt()
                    }
                }
            }
        }

    override fun getTotalItemsInShoppingCart(): Flow<Int> =
        shoppingCartDao.getTotalItemsInShoppingCart()

    override fun fetchShoppingItems(): Flow<List<ShoppingItemDomain>> =
        shoppingCartDao.getShoppingCart().map { shoppingItemData ->
            shoppingItemData.map { it.toDomain() }
        }
}