package com.example.youverifyassessment.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.youverifyassessment.data.local.db.entities.ShoppingCartEntity
import com.example.youverifyassessment.data.local.db.relations.ShoppingItemEntityData
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingCartEntity): Long

    @Query("SELECT COUNT(*) FROM shoppingCart WHERE productId = :productId")
    suspend fun getShoppingItemByProductId(productId: Int): Int

    @Query("SELECT SUM(quantity) FROM shoppingCart")
    fun getTotalNumberOfItemsInShoppingCart(): Flow<Long?>

    @Query("DELETE FROM shoppingCart WHERE productId = :productId")
    fun deleteShoppingItem(productId: Int): Int

    @Query("DELETE FROM shoppingCart")
    suspend fun clearAll(): Int

    @Query("SELECT * FROM products INNER JOIN shoppingCart ON products.id = shoppingCart.productId WHERE products.id IN (SELECT productId FROM products)")
    fun getShoppingCart(): Flow<List<ShoppingItemEntityData>>
}