package com.example.youverifyassessment.data.local.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.youverifyassessment.data.local.db.entities.ProductEntity
import com.example.youverifyassessment.data.local.db.entities.ShoppingCartEntity

data class ShoppingItemEntityData(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val shoppingCart: ShoppingCartEntity
)
