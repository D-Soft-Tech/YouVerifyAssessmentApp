package com.example.youverifyassessment.data.remote.dtos

data class ProductsResponseDtoItem(
    val category: CategoryDto,
    val creationAt: String,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val title: String,
    val updatedAt: String
)