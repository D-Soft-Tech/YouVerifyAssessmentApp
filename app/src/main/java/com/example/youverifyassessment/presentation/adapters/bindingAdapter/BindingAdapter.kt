package com.example.youverifyassessment.presentation.adapters.bindingAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.youverifyassessment.R
import com.example.youverifyassessment.domain.model.PaymentCard
import com.example.youverifyassessment.domain.model.PaymentCardOptions
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import com.example.youverifyassessment.utils.UtilsAndExtensions.formatCurrency
import com.google.android.material.button.MaterialButton

@BindingAdapter("android:loadProfilePicture")
fun ImageView.loadProfilePicture(profilePhotoUrl: String? = "") {
    if (profilePhotoUrl.isNullOrBlank()) {
        load(R.drawable.user_profile_picture) {
            crossfade(true)
            error(R.drawable.img_not_found)
            placeholder(R.drawable.small_circular_progress_bar_bg)
        }
    } else {
        load(profilePhotoUrl) {
            crossfade(true)
            error(R.drawable.img_not_found)
            placeholder(R.drawable.small_circular_progress_bar_bg)
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("android:formatAmount")
fun TextView.formatAmount(price: String?) {
    text = "₦${formatCurrency(price ?: "")}"
}

@BindingAdapter("android:totalPriceFromShoppingList")
fun TextView.totalPriceFromShoppingList(shoppingItemsList: List<ShoppingItemDomain>?) {
    shoppingItemsList?.let {
        text = it.fold(0.00) { acc: Double, cartItem: ShoppingItemDomain ->
            acc + (cartItem.totalPrice.toInt())
        }.toString()
    }
}

@BindingAdapter("android:setTotalPayableAmount")
fun MaterialButton.setTotalPayableAmount(shoppingItemsList: List<ShoppingItemDomain>?) {
    shoppingItemsList?.let {
        text = this.context.getString(
            R.string.pay_total_place_holder,
            it.fold(0.00) { acc: Double, cartItem: ShoppingItemDomain ->
                acc + (cartItem.totalPrice.toInt())
            }.toString()
        )
    }
}

@BindingAdapter("android:shoppingItemCountFromShoppingItemsList")
fun TextView.shoppingItemCountFromShoppingItemsList(shoppingItemsList: List<ShoppingItemDomain>?) {
    shoppingItemsList?.let { shoppingList ->
        text = shoppingList.sumOf { it.quantity.toInt() }.toString()
    }
}

@BindingAdapter("android:loadProductImage")
fun ImageView.loadProductImage(productImageUrl: String?) {
    Log.d("URL_LINK", productImageUrl ?: "NULL WAS GOTTEN")
    productImageUrl?.let {
        load(it) {
            crossfade(true)
            error(R.drawable.img_not_found)
            placeholder(R.drawable.small_circular_progress_bar_bg)
        }
    } ?: run {
        load(R.drawable.img_not_found)
    }
}

@BindingAdapter("android:setCardHolderNameWithCardScheme")
fun TextView.setCardHolderNameWithCardScheme(paymentCard: PaymentCard?) {
    paymentCard?.let {
        text = context.getString(
            R.string.card_holder_name_and_card_scheme,
            paymentCard.cardHolderName,
            paymentCard.cardType
        )
    }
}

@BindingAdapter("android:setMaskedPanWithBankName")
fun TextView.setMaskedPanWithBankName(paymentCard: PaymentCard?) {
    paymentCard?.let {
        text = context.getString(
            R.string.card_masked_pan_and_bank_name,
            paymentCard.cardMaskedPan,
            paymentCard.cardTitle
        )
    }
}

@BindingAdapter("android:setAppropriateCardSchemeLogo")
fun ImageView.setAppropriateCardSchemeLogo(paymentCard: PaymentCard?) {
    paymentCard?.let {
        when (it.cardType) {
            PaymentCardOptions.MASTER_CARD.cardScheme -> {
                load(R.drawable.ic_master_card)
            }

            PaymentCardOptions.VERVE_CARD.cardScheme -> {
                load(R.drawable.ic_verve_card)
            }

            else -> {
                load(R.drawable.ic_visa_card)
            }
        }
    }
}

