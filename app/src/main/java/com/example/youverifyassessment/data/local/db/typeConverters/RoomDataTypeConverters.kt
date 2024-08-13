package com.example.youverifyassessment.data.local.db.typeConverters

import androidx.room.TypeConverter
import com.example.youverifyassessment.data.local.db.entities.ProductCategoryEntity
import com.example.youverifyassessment.data.local.db.entities.ProductEntity
import com.example.youverifyassessment.data.local.db.entities.ShoppingCartEntity
import com.example.youverifyassessment.domain.model.ProductCategoryDomain
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.utils.AppConstants.PRODUCT_IMAGES_SEPARATOR
import com.example.youverifyassessment.utils.ModelMapper.toDomain
import com.example.youverifyassessment.utils.ModelMapper.toEntity
import com.google.gson.Gson

class RoomDataTypeConverters {
    companion object {
        @JvmStatic
        @TypeConverter
        fun fromProductCategoryEntity(category: ProductCategoryEntity): String =
            Gson().toJson(category)

        @JvmStatic
        @TypeConverter
        fun toProductCategoryEntity(productCategoryEntityAsString: String): ProductCategoryEntity =
            Gson().fromJson(productCategoryEntityAsString, ProductCategoryEntity::class.java)

        @JvmStatic
        @TypeConverter
        fun fromListOfImages(images: List<String>): String =
            images.joinToString(PRODUCT_IMAGES_SEPARATOR)

        @JvmStatic
        @TypeConverter
        fun toListOfImages(listOfImagesAsASingleString: String): List<String> =
            listOfImagesAsASingleString.split(PRODUCT_IMAGES_SEPARATOR)

        @JvmStatic
        @TypeConverter
        fun fromProductDomain(productsDomain: ProductsDomain): ProductEntity =
            productsDomain.toEntity()

        @JvmStatic
        @TypeConverter
        fun toProductDomain(productEntity: ProductEntity): ProductsDomain = productEntity.toDomain()

        @JvmStatic
        @TypeConverter
        fun fromProductCategoryDomain(productCategoryDomain: ProductCategoryDomain): String =
            Gson().toJson(productCategoryDomain)

        @JvmStatic
        @TypeConverter
        fun toProductCategoryDomain(productCategoryDomainAsString: String): ProductCategoryDomain =
            Gson().fromJson(productCategoryDomainAsString, ProductCategoryDomain::class.java)
    }
}