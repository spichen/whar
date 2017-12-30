package com.zigzag.whar.common

import java.util.regex.Pattern

/**
 * Created by salah on 31/12/17.
 */

object Utils {
    fun isValidMobile(phone: CharSequence): Boolean {
        var check = false
        check = if (!Pattern.matches("[a-zA-Z]+", phone)) {
            !(phone.length < 6 || phone.length > 13)
        } else {
            false
        }
        return check
    }
}
