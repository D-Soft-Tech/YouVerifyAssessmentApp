package com.example.youverifyassessment.domain.pagination

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.youverifyassessment.data.local.db.database.YouVerifyAppDatabase
import com.example.youverifyassessment.data.remote.apis.ProductsApiService
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.utils.AppConstants.PAGE_SIZE
import com.example.youverifyassessment.utils.ModelMapper.toDomain
import com.example.youverifyassessment.utils.ModelMapper.toEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductsRemoteMediator(
    private val database: YouVerifyAppDatabase,
    private val apiService: ProductsApiService
) : RemoteMediator<Int, ProductsDomain>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductsDomain>
    ): MediatorResult {
        val offSet = when (loadType) {
            LoadType.REFRESH -> {
                Log.d("CHECKING_LOAD_T", "LoadType.REFRESH")
                0
            }
            LoadType.PREPEND -> {
                Log.d("CHECKING_LOAD_T", "LoadType.PREPEND")
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                Log.d("CHECKING_LOAD_T", "LoadType.APPEND")
                val lastItemInDb = database.getProductsDao().getLastProduct()
                val lastLoadedItem = state.lastItemOrNull()
                val lastItemOrNull = lastItemInDb.getOrNull(0) ?: lastLoadedItem
                val lastItem = lastItemOrNull
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.offSet.toInt() + PAGE_SIZE
            }
        }

        try {
            val response = apiService.getProducts(offSet, PAGE_SIZE)
            val products = response.body()!!.let { productDto ->
                productDto.map { it.toDomain(offSet) }
            }
            val endOfPaginationReached = products.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getProductsDao().clearAll()
                }
                database.getProductsDao().insertProduct(products.map { it.toEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}