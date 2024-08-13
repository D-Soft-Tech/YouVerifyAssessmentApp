package com.example.youverifyassessment.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetailsDomain(
    val userId: String,
    val email: String? = "",
    val userPhoneNumber: String? = "",
    val profilePhotoUrl: String? = "",
    val userDisplayName: String? = ""
) : Parcelable