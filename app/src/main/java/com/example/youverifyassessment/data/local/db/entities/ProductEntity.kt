package com.example.youverifyassessment.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.youverifyassessment.utils.AppConstants.PRODUCT_TABLE_NAME

@Entity(PRODUCT_TABLE_NAME)
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val category: ProductCategoryEntity,
    val images: List<String>,
    val price: Int,
    val title: String,
    val productDescription: String
)