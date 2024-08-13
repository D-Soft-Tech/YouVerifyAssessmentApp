package com.example.youverifyassessment.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.DecimalFormat

class Utils {

    fun formatCurrency(value: Any): String {
        val valueToBeFormatted: Number =
            if (value is String) value.toDouble() else value as Number
        val df = DecimalFormat("##,###,##0.00")
        return df.format(valueToBeFormatted)
    }

    // used for hiding input keyboard
    fun hideKeyboard(context: Context?, view: View?) {
        if (context != null) {
            val imm =
                context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (view != null) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}