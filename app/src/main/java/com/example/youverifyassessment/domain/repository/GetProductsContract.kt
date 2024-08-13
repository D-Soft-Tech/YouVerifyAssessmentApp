package com.example.youverifyassessment.domain.repository

import androidx.paging.Pager
import com.example.youverifyassessment.domain.model.ProductsDomain

interface GetProductsContract {
    fun getProducts(offSet: Int, limit: Int = 20): Pager<Int, ProductsDomain>
}