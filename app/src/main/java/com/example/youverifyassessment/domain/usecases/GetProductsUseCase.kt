package com.example.youverifyassessment.domain.usecases

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.youverifyassessment.data.local.db.database.YouVerifyAppDatabase
import com.example.youverifyassessment.data.remote.apis.ProductsApiService
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.domain.pagination.ProductsRemoteMediator
import com.example.youverifyassessment.domain.repository.GetProductsContract
import com.example.youverifyassessment.utils.AppConstants.PAGE_SIZE
import com.example.youverifyassessment.utils.AppConstants.PREFETCH_DISTANCE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProductsUseCase @Inject constructor(
    private val youVerifyAppDatabase: YouVerifyAppDatabase,
    private val productsApiService: ProductsApiService
) : GetProductsContract {
    @OptIn(ExperimentalPagingApi::class)
    override fun getProducts(offSet: Int, limit: Int): Pager<Int, ProductsDomain> {
        val pagingSrcFactory = { youVerifyAppDatabase.getProductsDao().getProducts() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            remoteMediator = ProductsRemoteMediator(youVerifyAppDatabase, productsApiService),
            pagingSourceFactory = pagingSrcFactory
        )
    }
}