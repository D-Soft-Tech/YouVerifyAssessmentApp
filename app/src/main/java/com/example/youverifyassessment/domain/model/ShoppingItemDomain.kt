package com.example.youverifyassessment.domain.model

data class ShoppingItemDomain(
    val shoppingCartId: String,
    val product: ProductsDomain,
    var quantity: String,
    val totalPrice: String
)
