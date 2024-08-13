package com.example.youverifyassessment.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategoryDomain(
    val id: String,
    val image: String,
    val name: String,
): Parcelable