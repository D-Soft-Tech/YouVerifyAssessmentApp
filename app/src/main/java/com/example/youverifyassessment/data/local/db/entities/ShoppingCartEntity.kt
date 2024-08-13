package com.example.youverifyassessment.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.youverifyassessment.utils.AppConstants.SHOPPING_CART_TABLE_NAME

@Entity(SHOPPING_CART_TABLE_NAME)
data class ShoppingCartEntity(
    @PrimaryKey(autoGenerate = true)
    val shoppingCartId: Int?,
    val productId: Int,
    val quantity: Int
)