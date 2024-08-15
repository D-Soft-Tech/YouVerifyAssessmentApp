package com.example.youverifyassessment.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Patterns
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.youverifyassessment.R
import com.example.youverifyassessment.domain.DeviceUtilsContract
import java.text.DecimalFormat


object UtilsAndExtensions {
    fun Context.getLoadingAlertDialog(): Dialog = Dialog(this).apply {
        setContentView(R.layout.fragment_loading_dialog)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
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

    fun formatCurrency(value: Any): String {
        val valueToBeFormatted: Number =
            if (value is String) value.toDouble() else value as Number
        val df = DecimalFormat("##,###,##0.00")
        return df.format(valueToBeFormatted)
    }

    fun ImageButton.toggleCartActionButton(isInCart: Boolean) {
        if (isInCart) {
            this.setImageResource(R.drawable.ic_added_to_cart)
        } else {
            this.setImageResource(R.drawable.ic_add_to_cart)
        }
    }

    fun String.maskCardPan(): String {
        if (length <= 10) return this
        val first6 = take(6)
        val last4 = take(4)
        val middleChars = "*".repeat(length - 10)
        return first6 + middleChars + last4
    }

    fun Context.runIfConnected(deviceUtilsContract: DeviceUtilsContract, actionToRun: () -> Unit) {
        if (deviceUtilsContract.isConnectionAvailable()) {
            actionToRun.invoke()
        } else {
            showToast(getString(R.string.no_connection))
        }
    }
}