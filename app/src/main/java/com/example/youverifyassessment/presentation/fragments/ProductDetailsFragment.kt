package com.example.youverifyassessment.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentProductDetailsBinding
import com.example.youverifyassessment.domain.model.Review
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.ReviewsAdapter
import com.example.youverifyassessment.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var productImagesViewPager: ViewPager2
    private lateinit var productImagesViewPagerAdapter: FragmentStateAdapter
    private lateinit var reviewsRecyclerViewAdapter: ReviewsAdapter
    private var pageChangeCallBack: ViewPager2.OnPageChangeCallback? = null

    @Inject
    lateinit var utils: Utils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productDetailsMaterialToolbar.setupWithNavController(findNavController())

        bindViewsToValuesAndActions()

        setUpBottomSheetDialog()

        initProductImagesAdapter()

        initProductImagesViewPagerAdapter()

        setProductImagesViewPagerCompositePageTransformer()

        setUpTabLayoutMediator()

        initReviewsAdapter()

        initRecyclerViewAdapter()

    }

    @SuppressLint("SetTextI18n")
    private fun bindViewsToValuesAndActions() {

        binding.productDetailsShoppingCartLAV.setOnClickListener {
            val action = ProductDetailsFragmentDirections
                .actionProductDetailsFragmentToCheckOutFragment()
            findNavController().navigate(action)
        }

        binding.productDetailsNameTV.text = args.selectedProduct.title
        binding.productExtraDetailsTV.text = args.selectedProduct.productDescription
        binding.productDetailsPriceTV.text = "₦${utils.formatCurrency(args.selectedProduct.price)}"
        binding.productDetailsRB.progress = 4 / binding.productDetailsRB.max
    }

    private fun setUpBottomSheetDialog() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.productDetailsBottomSheetDialog)
        bottomSheetBehavior.peekHeight = 550
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.showBottomSheetButton.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.showBottomSheetButton.visibility = View.INVISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                when (slideOffset) {
                    in -1F..0F -> {
                        binding.showBottomSheetButton.visibility = View.VISIBLE
                        binding.showBottomSheetButton.alpha = abs(slideOffset)
                    }
                }
            }

        })
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.reviewsRV.visibility = View.INVISIBLE
                        binding.productDetailsReviewTV.visibility = View.INVISIBLE
                        binding.productsDetailsReviewTextUnderlineView.visibility = View.INVISIBLE
                    }

                    else -> {
                        binding.reviewsRV.visibility = View.VISIBLE
                        binding.productDetailsReviewTV.visibility = View.VISIBLE
                        binding.productsDetailsReviewTextUnderlineView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                when (slideOffset) {
                    in 0F..1F -> {
                        binding.reviewsRV.visibility = View.VISIBLE
                        binding.productDetailsReviewTV.visibility = View.VISIBLE
                        binding.productsDetailsReviewTextUnderlineView.visibility = View.VISIBLE
                        binding.reviewsRV.alpha = slideOffset
                        binding.productDetailsReviewTV.alpha = slideOffset
                        binding.productsDetailsReviewTextUnderlineView.alpha = slideOffset
                    }
                }
            }

        })
        binding.showBottomSheetButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setProductImagesViewPagerCompositePageTransformer() {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        productImagesViewPager.setPageTransformer(compositePageTransformer)
    }

    private fun initProductImagesViewPagerAdapter() {
        productImagesViewPager = binding.productDetailsImagesVP
        productImagesViewPager.adapter = productImagesViewPagerAdapter
        productImagesViewPager.offscreenPageLimit = 3
        productImagesViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun initProductImagesAdapter() {

        productImagesViewPagerAdapter =
            object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                private val fragments = arrayOf(
                    ProductImageFragment(""),
                    ProductImageFragment(""),
                    ProductImageFragment(""),
                    ProductImageFragment(""),
                    ProductImageFragment(""),
                    ProductImageFragment("")
                )
                override fun createFragment(position: Int) = fragments[position]
                override fun getItemCount(): Int = fragments.size
            }
    }

    private fun setUpTabLayoutMediator() {
        TabLayoutMediator(
            binding.productDetailsImagesTL,
            productImagesViewPager
        ) { tab, tabPosition ->
            tab.setIcon(R.drawable.ic_tab_indicator_inactive)
            pageChangeCallBack = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (tabPosition == position) {
                        tab.setIcon(R.drawable.ic_tab_indicator_active)
                    } else {
                        tab.setIcon(R.drawable.ic_tab_indicator_inactive)
                    }
                }
            }
            pageChangeCallBack?.let { productImagesViewPager.registerOnPageChangeCallback(it) }
        }.attach()
    }

    private fun initRecyclerViewAdapter() {
        binding.reviewsRV.adapter = reviewsRecyclerViewAdapter
        reviewsRecyclerViewAdapter.submitList(Review.reviews.toMutableList())
    }

    private fun initReviewsAdapter() {
        reviewsRecyclerViewAdapter = ReviewsAdapter { position: Int, itemAtPosition: Review ->

        }
    }
}