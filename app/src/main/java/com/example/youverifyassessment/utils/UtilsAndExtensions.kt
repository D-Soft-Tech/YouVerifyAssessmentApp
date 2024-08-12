package com.example.youverifyassessment.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.youverifyassessment.R


object UtilsAndExtensions {
    fun Context.getLoadingAlertDialog(): Dialog = Dialog(this).apply {
        setContentView(R.layout.fragment_loading_dialog)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setCancelable(false)
        create()
    }

    fun Context.showToast(message: String, isLongToast: Boolean = false) {
        val toastLength: Int = if (isLongToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(this, message, toastLength).show()
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}