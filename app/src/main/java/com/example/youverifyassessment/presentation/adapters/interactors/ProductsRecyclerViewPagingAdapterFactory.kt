package com.example.youverifyassessment.presentation.adapters.interactors

import com.example.youverifyassessment.presentation.adapters.pagingAdapter.AddToCartClickListener
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.ProductsClickListener
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.ProductsPagingAdapter
import com.example.youverifyassessment.utils.AppConstants.ON_ADD_TO_CART_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.ON_PRODUCT_CLICKED_DI_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ProductsRecyclerViewPagingAdapterFactory {
    fun createProductRecyclerViewPagingAdapter(
        @Assisted(ON_PRODUCT_CLICKED_DI_NAME) onProductClickListener: ProductsClickListener,
        @Assisted(ON_ADD_TO_CART_DI_NAME) onAddToCartClickListener: AddToCartClickListener
    ): ProductsPagingAdapter
}