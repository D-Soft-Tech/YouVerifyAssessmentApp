package com.example.youverifyassessment.utils

import com.example.youverifyassessment.data.local.db.entities.ProductCategoryEntity
import com.example.youverifyassessment.data.local.db.entities.ProductEntity
import com.example.youverifyassessment.data.local.db.entities.ShoppingCartEntity
import com.example.youverifyassessment.data.local.db.relations.ShoppingItemEntityData
import com.example.youverifyassessment.data.remote.dtos.CategoryDto
import com.example.youverifyassessment.data.remote.dtos.ProductsResponseDtoItem
import com.example.youverifyassessment.domain.model.ProductCategoryDomain
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.domain.model.UserDetailsDomain
import com.google.firebase.auth.FirebaseUser

object ModelMapper {
    fun FirebaseUser.toDomain(): UserDetailsDomain = UserDetailsDomain(
        uid, email, phoneNumber, photoUrl?.path, displayName
    )

    fun ProductsResponseDtoItem.toEntity(offSet: Int): ProductEntity = ProductEntity(
        id, category.toEntity(), images, price, title, description, offSet
    )

    fun ProductsResponseDtoItem.toDomain(offSet: Int): ProductsDomain = ProductsDomain(
        id.toString(), category.toDomain(), images, price.toString(), title, description, offSet.toString()
    )

    fun CategoryDto.toEntity(): ProductCategoryEntity = ProductCategoryEntity(
        id, image, name
    )

    fun CategoryDto.toDomain(): ProductCategoryDomain = ProductCategoryDomain(
        id.toString(), image, name
    )

    fun ProductCategoryDomain.toEntity(): ProductCategoryEntity = ProductCategoryEntity(
        id.toInt(), image, name
    )

    fun ProductCategoryEntity.toDomain(): ProductCategoryDomain =
        ProductCategoryDomain(id.toString(), image, name)

    fun ProductEntity.toDomain(): ProductsDomain =
        ProductsDomain(
            id.toString(),
            category.toDomain(),
            images,
            price.toString(),
            title,
            productDescription,
            offSet.toString()
        )

    fun ProductsDomain.toEntity(): ProductEntity = ProductEntity(
        id.toInt(), category.toEntity(), images, price.toInt(), title, productDescription
    )

    fun ShoppingItemEntityData.toDomain(): ShoppingItemDomain =
        ShoppingItemDomain(
            shoppingCart.shoppingCartId!!.toString(),
            product.toDomain(),
            shoppingCart.quantity.toString(),
            totalPrice = (shoppingCart.quantity * product.price).toString()
        )

    fun ProductsDomain.createFirstShoppingItem(): ShoppingItemDomain =
        ShoppingItemDomain("null", this, "0", "")

    fun ShoppingItemDomain.createFirstShoppingItemEntity(): ShoppingCartEntity =
        ShoppingCartEntity(null, product.id.toInt(), 0)

    fun ShoppingItemDomain.createIncrementedOrDecrementedShoppingCartEntity(newQuantity: Int): ShoppingCartEntity =
        ShoppingCartEntity(shoppingCartId.toInt(), product.id.toInt(), newQuantity)
}