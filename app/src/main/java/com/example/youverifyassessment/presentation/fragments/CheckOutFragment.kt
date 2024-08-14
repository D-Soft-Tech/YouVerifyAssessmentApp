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
import com.example.youverifyassessment.domain.model.PaymentCardOptions
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.domain.model.mapToPaymentCard
import com.example.youverifyassessment.presentation.adapters.pagingAdapter.CartAdapter
import com.example.youverifyassessment.presentation.viewModels.AppViewModel
import com.example.youverifyassessment.utils.Utils
import com.example.youverifyassessment.utils.UtilsAndExtensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CheckOutFragment : Fragment() {
    private lateinit var binding: FragmentCheckOutBinding
    private val appViewModel: AppViewModel by activityViewModels()
    private lateinit var checkOutRecyclerViewAdapter: CartAdapter
    private lateinit var paymentCard: List<PaymentCard>
    private var totalAmount: Double = 0.00

    @Inject
    lateinit var utils: Utils

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
        setSelectedPaymentCard(
            paymentCard.find { paymentCard -> paymentCard.selected }
        )
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
            val subtotal = result.fold(0.00) { acc: Double, cartItem: ShoppingItemDomain ->
                acc + (cartItem.totalPrice.toInt())
            }
            val shippingFee = 0.00
            totalAmount = subtotal + shippingFee
            binding.cartQuantityTV.text = result.sumOf { it.quantity.toInt() }.toString()
            binding.checkoutShippingFeeTV.text = "₦${utils.formatCurrency(shippingFee)}"
            binding.checkoutSubTotalTV.text = "₦${utils.formatCurrency(subtotal)}"
            binding.checkoutTotalTV.text = "₦${utils.formatCurrency(totalAmount)}"
            binding.checkoutPayButton.text = "Pay (₦${utils.formatCurrency(totalAmount)})"

            checkOutRecyclerViewAdapter.submitList(result)
        }
    }

    private fun setSelectedPaymentCard(selectedPaymentCard: PaymentCard?) {
        when (selectedPaymentCard?.cardType) {
            PaymentCardOptions.MASTER_CARD.name -> {
                binding.checkoutPaymentCardIV.setImageResource(R.drawable.ic_master_card)
            }

            PaymentCardOptions.VERVE_CARD.name -> {
                binding.checkoutPaymentCardIV.setImageResource(R.drawable.ic_verve_card)
            }

            PaymentCardOptions.VISA_CARD.name -> {
                binding.checkoutPaymentCardIV.setImageResource(R.drawable.ic_visa_card)
            }
        }
        binding.checkoutPaymentCardNameTV.text = selectedPaymentCard?.cardType?.lowercase(
            Locale.getDefault()
        )
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        binding.checkoutPaymentCardNumberTV.text =
            selectedPaymentCard?.cardNumber?.chunked(4)?.joinToString(" ")
    }

    private fun initCartRecyclerViewAdapter() {
        binding.checkOutItemsRV.adapter = checkOutRecyclerViewAdapter
    }

    private fun initCartAdapter() {
        checkOutRecyclerViewAdapter = CartAdapter(
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