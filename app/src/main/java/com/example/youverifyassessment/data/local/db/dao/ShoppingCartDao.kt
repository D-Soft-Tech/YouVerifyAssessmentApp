package com.example.youverifyassessment.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.youverifyassessment.data.local.db.relations.ShoppingItemEntityData
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItemDomain): Int

    @Query("DELETE FROM shoppingCart WHERE shoppingCartId = :shoppingItemId")
    fun deleteShoppingItem(shoppingItemId: Int): Int

    @Query("DELETE FROM shoppingCart")
    suspend fun clearAll(): Int

    @Transaction
    @Query("SELECT * FROM products WHERE id IN (SELECT productId FROM shoppingCart)")
    fun getShoppingCart(): Flow<List<ShoppingItemEntityData>>
}