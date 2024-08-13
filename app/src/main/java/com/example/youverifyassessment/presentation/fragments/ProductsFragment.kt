package com.example.youverifyassessment.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentProductsBinding
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.presentation.adapters.interactors.ProductsRecyclerViewPagingAdapterFactory
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.AddToCartClickListener
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.ProductsClickListener
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.ProductsPagingAdapter
import com.example.youverifyassessment.presentation.viewModels.AppViewModel
import com.example.youverifyassessment.utils.ModelMapper.createFirstShoppingItem
import com.example.youverifyassessment.utils.UtilsAndExtensions.toggleCartActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var lottieAnimView: LottieAnimationView
    private lateinit var shoppingCartCounter: TextView
    private val appViewModel: AppViewModel by activityViewModels()

    @Inject
    lateinit var productsRecyclerViewPagingAdapterFactory: ProductsRecyclerViewPagingAdapterFactory
    private lateinit var productsPagingAdapter: ProductsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        binding.apply {
            appViewModel = this@ProductsFragment.appViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        val onProductSelected = object : ProductsClickListener {
            override fun invoke(selectedProduct: ProductsDomain) {
                val action =
                    ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                        selectedProduct
                    )
                findNavController().navigate(action)
            }
        }
        val onAddToCart = object : AddToCartClickListener {
            override fun invoke(product: ProductsDomain, clickedView: View) {
                val shoppingCartItem = product.createFirstShoppingItem()
                val isIncrease = (clickedView as ImageButton).toggleCartActionButton()
                lottieAnimView.playAnimation()
                appViewModel.getTotalItemsInShoppingCart()
                appViewModel.insertUpdateOrRemoveShoppingItem(shoppingCartItem, isIncrease)
            }
        }
        productsPagingAdapter =
            productsRecyclerViewPagingAdapterFactory.createProductRecyclerViewPagingAdapter(
                onProductSelected,
                onAddToCart
            )
        recyclerView.apply {
            adapter = productsPagingAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appViewModel.products.collect {
                    productsPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            recyclerView = productsRV
            lottieAnimView = productsShoppingCartLAV
            shoppingCartCounter = productsCartQuantityTV
        }
    }
}