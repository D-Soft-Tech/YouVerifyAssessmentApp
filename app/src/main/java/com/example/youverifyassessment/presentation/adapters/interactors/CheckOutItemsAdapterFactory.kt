package com.example.youverifyassessment.presentation.adapters.interactors

import com.example.youverifyassessment.presentation.adapters.pagingAdapter.CheckOutItemsAdapter
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.ShoppingCartRVItemClickListener
import com.example.youverifyassessment.utils.AppConstants.CART_RV_CLICK_LISTENER_DI_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface CheckOutItemsAdapterFactory {
    fun createCheckOutItemsAdapter(
        @Assisted(CART_RV_CLICK_LISTENER_DI_NAME) cartItemClickListener: ShoppingCartRVItemClickListener
    ): CheckOutItemsAdapter
}