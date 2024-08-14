package com.example.youverifyassessment.presentation.adapters.pagingAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.youverifyassessment.databinding.ItemCartItemBinding
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.presentation.adapters.bindingAdapter.loadProductImage
import com.example.youverifyassessment.utils.Utils

class CartAdapter(
    private val onItemClicked: (position: Int, itemAtPosition: ShoppingItemDomain) -> Unit,
    private val onMinusButtonClicked: (position: Int, itemAtPosition: ShoppingItemDomain) -> Unit,
    private val onPlusButtonClicked: (position: Int, itemAtPosition: ShoppingItemDomain) -> Unit,
    private val utils: Utils
) : ListAdapter<ShoppingItemDomain, CartAdapter.CartItemVH>(object :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemVH {
        val binding =
            ItemCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemVH(
            binding,
            onItemClick = { position ->
                try {
                    val itemAtPosition = currentList[position]
                    this.onItemClicked(position, itemAtPosition)
                } catch (e: Exception) {
                }

            },
            onMinusButtonClick = { position ->
                try {
                    val itemAtPosition = currentList[position]
                    this.onMinusButtonClicked(position, itemAtPosition)
                } catch (e: Exception) {
                }

            },
            onPlusButtonClick = { position ->
                try {
                    val itemAtPosition = currentList[position]
                    this.onPlusButtonClicked(position, itemAtPosition)
                } catch (e: Exception) {
                }

            }
        )

    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: CartItemVH, position: Int) {
        val itemAtPosition = currentList[position]
        holder.bind(itemAtPosition)
    }


    inner class CartItemVH(
        private val binding: ItemCartItemBinding,
        onItemClick: (position: Int) -> Unit,
        onMinusButtonClick: (position: Int) -> Unit,
        onPlusButtonClick: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
            binding.cartItemMinusButton.setOnClickListener {
                onMinusButtonClick(bindingAdapterPosition)
            }
            binding.cartItemPlusButton.setOnClickListener {
                onPlusButtonClick(bindingAdapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(cartItem: ShoppingItemDomain) {
            with(binding) {
                cartItemNameTV.text = cartItem.product.title
                cartItemDescriptionTV.text = cartItem.product.productDescription
                cartItemIV.loadProductImage(cartItem.product.images.getOrNull(0))
                cartItemPrice.text = "₦${utils.formatCurrency(cartItem.totalPrice)}"
                cartItemQuantityTV.text = cartItem.quantity
            }
        }
    }
}