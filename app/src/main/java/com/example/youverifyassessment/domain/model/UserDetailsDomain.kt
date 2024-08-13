package com.example.youverifyassessment.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetailsDomain(
    val userId: String,
    val email: String? = "oloyedeadebayoolawale@gmail.com",
    val userPhoneNumber: String? = "+2349075771869",
    val profilePhotoUrl: String? = "",
    val userDisplayName: String? = "Adebayo"
) : Parcelable