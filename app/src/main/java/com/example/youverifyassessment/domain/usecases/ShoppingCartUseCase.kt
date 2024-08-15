package com.example.youverifyassessment.domain.usecases

import android.util.Log
import androidx.room.withTransaction
import com.example.youverifyassessment.data.local.db.dao.ProductDao
import com.example.youverifyassessment.data.local.db.dao.ShoppingCartDao
import com.example.youverifyassessment.data.local.db.database.YouVerifyAppDatabase
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.domain.repository.ShoppingCartContract
import com.example.youverifyassessment.utils.ModelMapper.createFirstShoppingItemEntity
import com.example.youverifyassessment.utils.ModelMapper.createIncrementedOrDecrementedShoppingCartEntity
import com.example.youverifyassessment.utils.ModelMapper.toDomain
import com.example.youverifyassessment.utils.ModelMapper.toEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCartUseCase @Inject constructor(
    private val database: YouVerifyAppDatabase,
    private val shoppingCartDao: ShoppingCartDao,
    private val productDao: ProductDao
) : ShoppingCartContract {
    override suspend fun insertUpdateOrRemoveShoppingItem(
        shoppingItem: ShoppingItemDomain,
        isIncrease: Boolean
    ): Int =
        when {
            shoppingItem.quantity.toInt() == 0 && isIncrease -> {
                database.withTransaction {
                    productDao.insertProduct(
                        arrayListOf(
                            shoppingItem.copy(quantity = "1").product.toEntity()
                                .copy(isInCart = true)
                        )
                    )
                    val shoppingItemEntity =
                        shoppingItem.copy(quantity = "1").createFirstShoppingItemEntity()
                    shoppingCartDao.insertShoppingItem(shoppingItemEntity).toInt()
                }
            }

            shoppingItem.quantity.toInt() == 0 && !isIncrease -> {
                database.withTransaction {
                    productDao.insertProduct(
                        arrayListOf(
                            shoppingItem.product.toEntity().copy(isInCart = false)
                        )
                    )
                    shoppingCartDao.deleteShoppingItem(shoppingItem.product.id.toInt())
                }
            }

            else -> {
                database.withTransaction {
                    val getIfShoppingItemExist =
                        shoppingCartDao.getShoppingItemByProductId(shoppingItem.product.id.toInt()) > 0
                    if (getIfShoppingItemExist) {
                        database.withTransaction {
                            productDao.insertProduct(
                                arrayListOf(
                                    shoppingItem.product.toEntity().copy(isInCart = isIncrease)
                                )
                            )
                            val shoppingItemEntity =
                                shoppingItem.createIncrementedOrDecrementedShoppingCartEntity(
                                    isIncrease
                                )
                            if (shoppingItemEntity.quantity > 0) {
                                shoppingCartDao.insertShoppingItem(shoppingItemEntity).toInt()
                            } else {
                                shoppingCartDao.deleteShoppingItem(shoppingItemEntity.productId)
                            }
                        }
                    } else {
                        database.withTransaction {
                            productDao.insertProduct(
                                arrayListOf(
                                    shoppingItem.product.toEntity().copy(isInCart = isIncrease)
                                )
                            )
                            val shoppingItemEntity = shoppingItem.createFirstShoppingItemEntity()
                            shoppingCartDao.insertShoppingItem(shoppingItemEntity).toInt()
                        }
                    }
                }
            }
        }

    override fun getTotalItemsInShoppingCart(): Flow<Long?> =
        shoppingCartDao.getTotalNumberOfItemsInShoppingCart().catch {
            emit(0L)
        }

    override suspend fun clearShoppingCart(shoppingItems: List<ProductsDomain>): Flow<Int> {
        return database.withTransaction {
            productDao.insertProduct(shoppingItems.map { it.copy(isInCart = false).toEntity()})
            shoppingCartDao.clearAll()
        }.let {
            flowOf(it)
        }
    }

    override fun fetchShoppingItems(): Flow<List<ShoppingItemDomain>> =
        shoppingCartDao.getShoppingCart().map { shoppingItemData ->
            shoppingItemData.map { it.toDomain() }
        }
}