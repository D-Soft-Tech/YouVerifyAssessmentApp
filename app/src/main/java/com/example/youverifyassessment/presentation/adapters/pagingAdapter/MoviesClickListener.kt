package com.example.youverifyassessment.presentation.adapters.pagingAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.ItemProductBinding
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.presentation.adapters.interactors.ProductRecyclerViewBindingInterface
import com.example.youverifyassessment.utils.AppConstants.ON_ADD_TO_CART_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.ON_PRODUCT_CLICKED_DI_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Named

typealias ProductsClickListener = (selectedProduct: ProductsDomain) -> Unit
typealias AddToCartClickListener = (product: ProductsDomain, clickedView: View) -> Unit

class ProductsPagingAdapter @AssistedInject constructor(
    @Assisted(ON_PRODUCT_CLICKED_DI_NAME) private val clickListener: ProductsClickListener,
    @Assisted(ON_ADD_TO_CART_DI_NAME) private val addToCartClickListener: AddToCartClickListener
) :
    PagingDataAdapter<ProductsDomain, ProductsPagingAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(val view: ViewDataBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(
            bindingInterface: ProductRecyclerViewBindingInterface,
        ) {
            bindingInterface.bindData(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = DataBindingUtil.inflate<ItemProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_product,
            parent,
            false,
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bindingData: ProductItemPagingRvBindingInterfaceImpl? =
            getItem(position)?.let { ProductItemPagingRvBindingInterfaceImpl(it) }
        if (bindingData != null) {
            holder.apply {
                bind(bindingData)
                itemView.setOnClickListener {
                    getItem(position)?.let { it1 ->
                        clickListener.invoke(it1)
                    }
                }
                (holder.view as ItemProductBinding).addToCartButton.setOnClickListener {
                    getItem(position)?.let { product ->
                        addToCartClickListener.invoke(product, it)
                    }
                }
            }
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ProductsDomain>() {
            override fun areItemsTheSame(
                oldItem: ProductsDomain,
                newItem: ProductsDomain
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ProductsDomain,
                newItem: ProductsDomain
            ): Boolean =
                oldItem == newItem
        }
    }
}