package com.example.youverifyassessment.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.youverifyassessment.data.local.db.relations.ShoppingItemEntityData
import com.example.youverifyassessment.domain.model.ShoppingItemDomain

@Dao
interface ShoppingCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItemDomain)

    @Query("DELETE FROM shoppingCart WHERE shoppingCartId = :shoppingItemId")
    suspend fun deleteShoppingItem(shoppingItemId: Int): Int

    @Query("SELECT ALL <productId, category, > FROM products  ")
    suspend fun aaa(): List<ShoppingItemEntityData>

    @Query("DELETE FROM shoppingCart")
    suspend fun clearAll(): Int

    @Transaction
    @Query("SELECT * FROM products WHERE id IN (SELECT productId FROM shoppingCart)")
    suspend fun getShoppingCart(): List<ShoppingItemEntityData>
}