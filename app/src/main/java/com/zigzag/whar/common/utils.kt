package com.zigzag.whar.common

import android.content.Context
import java.util.regex.Pattern
import android.util.DisplayMetrics

/**
 * Created by salah on 31/12/17.
 */

object Utils {
    fun isValidMobile(phone: CharSequence): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            !(phone.length < 6 || phone.length > 13)
        } else {
            false
        }
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
