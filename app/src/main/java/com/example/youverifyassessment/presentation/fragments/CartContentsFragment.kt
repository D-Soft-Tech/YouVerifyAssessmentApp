package com.example.youverifyassessment.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentCartBinding
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.CartAdapter
import com.example.youverifyassessment.presentation.viewModels.AppViewModel
import com.example.youverifyassessment.utils.UtilsAndExtensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartContentsFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var cartRecyclerViewAdapter: CartAdapter
    private lateinit var cartProceedToCheckOutBtn: Button
    private lateinit var shoppingCartRv: RecyclerView

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
        initViews()
        binding.apply {
            this.appViewModel = this@CartContentsFragment.appViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        appViewModel.getShoppingCartFromDb()
        cartProceedToCheckOutBtn.setOnClickListener {
            appViewModel.totalItemsInCart.observe(viewLifecycleOwner) {
                it?.let {
                    if (it.toInt() > 1) {
                        val action =
                            CartContentsFragmentDirections.actionCartContentsFragmentToCheckOutFragment()
                        findNavController().navigate(action)
                    } else {
                        requireContext().showToast(getString(R.string.cart_is_empty), true)
                    }
                } ?: run { requireContext().showToast(getString(R.string.cart_is_empty), true) }
            }
        }

        initCartAdapter()
        initCartRecyclerViewAdapter()

        appViewModel.shoppingCart.observe(viewLifecycleOwner) { result ->
            if (result.isEmpty()) {
                cartProceedToCheckOutBtn.visibility = View.INVISIBLE
            } else {
                cartProceedToCheckOutBtn.visibility = View.VISIBLE
            }
            cartRecyclerViewAdapter.submitList(result)
        }
    }

    private fun initCartRecyclerViewAdapter() {
        shoppingCartRv.adapter = cartRecyclerViewAdapter
    }

    private fun initViews() {
        with(binding) {
            shoppingCartRv = cartRV
            cartProceedToCheckOutBtn = cartProceedToCheckOutButton
        }
    }

    private fun initCartAdapter() {
        cartRecyclerViewAdapter = CartAdapter(
            onItemClicked = { _: Int, clickedShoppingItem: ShoppingItemDomain ->
                val action =
                    CartContentsFragmentDirections.actionCartContentsFragmentToProductDetailsFragment(
                        clickedShoppingItem.product
                    )
                findNavController().navigate(action)
            }, onMinusButtonClicked = { _: Int, itemAtPosition: ShoppingItemDomain ->
                appViewModel.insertUpdateOrRemoveShoppingItem(
                    itemAtPosition.copy(quantity = (itemAtPosition.quantity.toInt()).toString()),
                    false
                )
                appViewModel.getShoppingCartFromDb()
            }, onPlusButtonClicked = { _: Int, itemAtPosition: ShoppingItemDomain ->
                appViewModel.insertUpdateOrRemoveShoppingItem(
                    itemAtPosition.copy(quantity = (itemAtPosition.quantity.toInt()).toString()),
                    true
                )
                appViewModel.getShoppingCartFromDb()
            }
        )
    }
}