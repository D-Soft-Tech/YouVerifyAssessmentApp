package com.example.youverifyassessment.presentation.adapters.pagingAdapter

import androidx.databinding.ViewDataBinding
import com.example.youverifyassessment.databinding.ItemProductBinding
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.presentation.adapters.interactors.ProductRecyclerViewBindingInterface

class ProductItemPagingRvBindingInterfaceImpl(private val item: ProductsDomain) :
    ProductRecyclerViewBindingInterface {
    override fun bindData(view: ViewDataBinding) {
        (view as ItemProductBinding).apply {
            product = item
        }
    }
}