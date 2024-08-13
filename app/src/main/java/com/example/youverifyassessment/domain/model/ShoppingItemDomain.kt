package com.example.youverifyassessment.domain.model

data class ShoppingItemDomain(
    val shoppingCartId: String,
    val product: ProductsDomain,
    val quantity: String,
    val totalPrice: String
)
