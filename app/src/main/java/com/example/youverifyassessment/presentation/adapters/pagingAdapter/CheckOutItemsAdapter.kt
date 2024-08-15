package com.example.youverifyassessment.presentation.adapters.pagingAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.youverifyassessment.databinding.ItemCheckoutItemBinding
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.utils.AppConstants.CART_RV_CLICK_LISTENER_DI_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

typealias ShoppingCartRVItemClickListener = (position: Int, itemAtPosition: ShoppingItemDomain) -> Unit

class CheckOutItemsAdapter @AssistedInject constructor(
    @Assisted(CART_RV_CLICK_LISTENER_DI_NAME) private val onItemClicked: ShoppingCartRVItemClickListener,
) : ListAdapter<ShoppingItemDomain, CheckOutItemsAdapter.CheckOutItemVH>(object :
    DiffUtil.ItemCallback<ShoppingItemDomain>() {

    override fun areItemsTheSame(
        oldItem: ShoppingItemDomain,
        newItem: ShoppingItemDomain
    ): Boolean {
        return oldItem.shoppingCartId == newItem.shoppingCartId
    }

    override fun areContentsTheSame(
        oldItem: ShoppingItemDomain,
        newItem: ShoppingItemDomain
    ): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutItemVH {
        val binding =
            ItemCheckoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckOutItemVH(
            binding,
            onItemClick = { position ->
                try {
                    val itemAtPosition = currentList[position]
                    onItemClicked(position, itemAtPosition)
                } catch (e: Exception) {
                    e.localizedMessage?.let { Log.d(this.javaClass.simpleName, it) }
                }
            }
        )
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: CheckOutItemVH, position: Int) {
        val itemAtPosition = currentList[position]
        holder.bind(itemAtPosition)
    }

    inner class CheckOutItemVH(
        private val binding: ItemCheckoutItemBinding,
        onItemClick: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(checkOutItem: ShoppingItemDomain) {
            with(binding) {
                this.shoppingItemDomain = checkOutItem
            }
        }
    }
}