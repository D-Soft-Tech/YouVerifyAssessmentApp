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

    fun ImageButton.toggleCartActionButton(): Boolean {
        var hasNotBeenAddedAndShouldNotBeRemoved = true
        drawable?.let {
            if (it.constantState!! == ContextCompat.getDrawable(
                    this.context,
                    R.drawable.ic_added_to_cart
                )!!.constantState
            ) {
                hasNotBeenAddedAndShouldNotBeRemoved = false
                this.setImageResource(R.drawable.ic_add_to_cart)
            } else {
                this.setImageResource(R.drawable.ic_added_to_cart)
            }
        }
        return hasNotBeenAddedAndShouldNotBeRemoved
    }
}