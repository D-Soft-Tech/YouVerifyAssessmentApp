package com.example.youverifyassessment.presentation.adapters.bindingAdapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.youverifyassessment.R
import com.example.youverifyassessment.utils.Utils

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
    text = "₦${Utils().formatCurrency(price ?: "")}"
}

@BindingAdapter("android:loadProductImage")
fun ImageView.loadProductImage(productImageUrl: String?) {
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

