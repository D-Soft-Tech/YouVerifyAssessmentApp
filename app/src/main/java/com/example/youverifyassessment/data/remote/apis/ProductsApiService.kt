package com.example.youverifyassessment.data.remote.apis

import com.example.youverifyassessment.data.remote.dtos.ProductsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApiService {
    @GET("api/v1/products")
    fun getProducts(
        @Query("offset") offSet: Int,
        @Query("limit") pageSize: Int
    ): Response<ProductsResponseDto>
}