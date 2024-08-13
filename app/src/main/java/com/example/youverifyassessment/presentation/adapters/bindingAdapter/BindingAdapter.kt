package com.example.youverifyassessment.presentation.adapters.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.youverifyassessment.R

@BindingAdapter("android:loadProfilePicture")
fun ImageView.loadProfilePicture(profilePhotoUrl: String? = "") {
    if (profilePhotoUrl.isNullOrBlank()) {
        load(R.drawable.user_profile_picture)
    } else {
        load(profilePhotoUrl)
    }
}

@BindingAdapter("android:loadProductImage")
fun ImageView.loadProductImage(productImageUrl: String?) {
    productImageUrl?.let {
        load(it)
    } ?: run {
        load(R.drawable.img_not_found)
    }
}

