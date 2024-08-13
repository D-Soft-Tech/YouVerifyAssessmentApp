package com.example.youverifyassessment.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.youverifyassessment.data.local.db.entities.ProductEntity
import com.example.youverifyassessment.domain.model.ProductsDomain

@Dao
interface ProductDao {
    @Upsert
    suspend fun insertProduct(products: List<ProductEntity>)

    @Query("SELECT * FROM products ORDER BY id DESC LIMIT 1")
    suspend fun getLastProduct(): List<ProductsDomain>

    @Query("DELETE FROM products")
    suspend fun clearAll(): Int

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getProducts(): PagingSource<Int, ProductsDomain>
}