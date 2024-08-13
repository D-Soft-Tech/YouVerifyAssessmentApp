package com.example.youverifyassessment.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsDomain(
    val id: String,
    val category: ProductCategoryDomain,
    val images: List<String>,
    val price: String,
    val title: String,
    val productDescription: String,
    val offSet: String
): Parcelable