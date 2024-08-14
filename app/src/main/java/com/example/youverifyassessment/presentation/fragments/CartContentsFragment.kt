package com.example.youverifyassessment.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentCartBinding
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.CartAdapter
import com.example.youverifyassessment.presentation.viewModels.AppViewModel
import com.example.youverifyassessment.utils.Utils
import com.example.youverifyassessment.utils.UtilsAndExtensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartContentsFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var cartRecyclerViewAdapter: CartAdapter

    @Inject
    lateinit var utils: Utils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appViewModel.getShoppingCart()
        binding.cartProceedToCheckOutButton.setOnClickListener {
            appViewModel.totalItemsInCart.observe(viewLifecycleOwner) {
                it?.let {
                    if (it.toInt() > 1) {
                        val action =
                            CartContentsFragmentDirections.actionCartContentsFragmentToCheckOutFragment()
                        findNavController().navigate(action)
                    } else {
                        requireContext().showToast(getString(R.string.cart_is_empty), true)
                    }
                }
            }
        }

        initCartAdapter()

        initCartRecyclerViewAdapter()

        appViewModel.shoppingCart.observe(viewLifecycleOwner) { result ->
            val subtotal = result.fold(0.00) { acc: Double, cartItem: ShoppingItemDomain ->
                acc + (cartItem.totalPrice.toInt())
            }
            val fee = 0.00
            if (result.isEmpty()) {
                binding.cartProceedToCheckOutButton.visibility = View.INVISIBLE
            } else {
                binding.cartProceedToCheckOutButton.visibility = View.VISIBLE
            }
            binding.cartQuantityTV.text = result.sumOf { it.quantity.toInt() }.toString()
            binding.cartShippingFeeTV.text = "₦${utils.formatCurrency(fee)}"
            binding.cartSubTotalTV.text = "₦${utils.formatCurrency(subtotal)}"
            binding.cartTotalTV.text = "₦${utils.formatCurrency(subtotal + fee)}"
            cartRecyclerViewAdapter.submitList(result)
        }
    }

    private fun initCartRecyclerViewAdapter() {
        binding.cartRV.adapter = cartRecyclerViewAdapter
    }

    private fun initCartAdapter() {
        cartRecyclerViewAdapter = CartAdapter(
            onItemClicked = { _: Int, _: ShoppingItemDomain ->

            }, onMinusButtonClicked = { _: Int, itemAtPosition: ShoppingItemDomain ->
                appViewModel.insertUpdateOrRemoveShoppingItem(
                    itemAtPosition.copy(quantity = (itemAtPosition.quantity.toInt() - 1).toString()),
                    false
                )
                appViewModel.getShoppingCart()
            }, onPlusButtonClicked = { _: Int, itemAtPosition: ShoppingItemDomain ->
                appViewModel.insertUpdateOrRemoveShoppingItem(
                    itemAtPosition.copy(quantity = (itemAtPosition.quantity.toInt() + 1).toString()),
                    true
                )
                appViewModel.getShoppingCart()
            },
            utils
        )
    }
}