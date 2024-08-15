package com.example.youverifyassessment.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentCheckOutBinding
import com.example.youverifyassessment.domain.model.PaymentCard
import com.example.youverifyassessment.domain.model.PaymentCardEntity
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.domain.model.mapToPaymentCard
import com.example.youverifyassessment.presentation.adapters.interactors.CheckOutItemsAdapterFactory
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.CheckOutItemsAdapter
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.ShoppingCartRVItemClickListener
import com.example.youverifyassessment.presentation.viewModels.AppViewModel
import com.example.youverifyassessment.utils.UtilsAndExtensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckOutFragment : Fragment() {
    private lateinit var binding: FragmentCheckOutBinding
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var checkOutRecyclerViewAdapter: CheckOutItemsAdapter
    private lateinit var paymentCard: List<PaymentCard>

    @Inject
    lateinit var checkOutItemsAdapterFactory: CheckOutItemsAdapterFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // override on back pressed
        activity?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!binding.checkOutProgressIndicator.isVisible) {
                        isEnabled = false
                        activity?.onBackPressed()
                    } else {
                        isEnabled = true
                        requireContext().showToast(getString(R.string.transaction_in_progress))
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_check_out, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentCard = PaymentCardEntity.paymentCardEntities.map { it.mapToPaymentCard() }
        // Bind Data to views
        binding.apply {
            this.appViewModel = this@CheckOutFragment.appViewModel
            this.paymentCard = this@CheckOutFragment.paymentCard.find { it.selected }
            lifecycleOwner = viewLifecycleOwner
        }
        binding.checkoutPayButton.setOnClickListener {
            //callPayStack()
            appViewModel.clearCart()
            val action =
                CheckOutFragmentDirections.actionCheckOutFragmentToSuccessFragment()
            findNavController().navigate(action)
        }

        initCartAdapter()
        initCartRecyclerViewAdapter()

        appViewModel.shoppingCart.observe(viewLifecycleOwner) { result ->
            checkOutRecyclerViewAdapter.submitList(result)
        }
    }

    private fun initCartRecyclerViewAdapter() {
        binding.checkOutItemsRV.adapter = checkOutRecyclerViewAdapter
    }

    private fun initCartAdapter() {
        checkOutRecyclerViewAdapter =
            checkOutItemsAdapterFactory.createCheckOutItemsAdapter(object :
                ShoppingCartRVItemClickListener {
                override fun invoke(position: Int, itemAtPosition: ShoppingItemDomain) {
                    val action =
                        CheckOutFragmentDirections.actionCheckOutFragmentToProductDetailsFragment(
                            itemAtPosition.product
                        )
                    findNavController().navigate(action)
                }
            })
    }
}