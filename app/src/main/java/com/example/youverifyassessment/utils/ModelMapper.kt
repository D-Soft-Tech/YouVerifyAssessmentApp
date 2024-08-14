package com.example.youverifyassessment.utils

import android.util.Log
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
import com.google.gson.Gson
import java.lang.Exception

object ModelMapper {
    fun FirebaseUser.toDomain(): UserDetailsDomain = UserDetailsDomain(
        uid, email, phoneNumber, photoUrl?.path, displayName
    )

    fun ProductsResponseDtoItem.toEntity(offSet: Int): ProductEntity = ProductEntity(
        id, category.toEntity(), images, price, title, description, offSet, false
    )

    fun ProductsResponseDtoItem.toDomain(offSet: Int): ProductsDomain = ProductsDomain(
        id.toString(),
        category.toDomain(),
        images,
        price.toString(),
        title,
        description,
        offSet.toString(),
        false
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
            offSet.toString(),
            isInCart
        )

    fun ProductsDomain.toEntity(): ProductEntity = ProductEntity(
        id.toInt(),
        category.toEntity(),
        images,
        price.toInt(),
        title,
        productDescription,
        offSet.toInt(),
        isInCart
    )

    fun ShoppingItemEntityData.toDomain(): ShoppingItemDomain =
        ShoppingItemDomain(
            shoppingCart.shoppingCartId!!.toString(),
            product.toDomain(),
            shoppingCart.quantity.toString(),
            totalPrice = (shoppingCart.quantity * product.price).toString()
        )

    fun ProductsDomain.createFirstShoppingItem(): ShoppingItemDomain =
        if (this.isInCart) {
            ShoppingItemDomain("null", this, "1", price)
        } else {
            ShoppingItemDomain("null", this, "0", price)
        }

    fun ShoppingItemDomain.createFirstShoppingItemEntity(): ShoppingCartEntity =
        ShoppingCartEntity(null, product.id.toInt(), quantity.toInt())

    fun ShoppingItemDomain.createIncrementedOrDecrementedShoppingCartEntity(isIncreaseTheQyt: Boolean): ShoppingCartEntity {
        val newQuantity = if (isIncreaseTheQyt) (quantity.toInt() + 1) else (quantity.toInt() - 1)
            Log.d("DATA_ERROR_1", Gson().toJson(this))
        return try {
            ShoppingCartEntity(shoppingCartId?.toInt(), product.id.toInt(), newQuantity)
        } catch (e: Exception) {
            ShoppingCartEntity(null, product.id.toInt(), newQuantity)
        }
    }
}