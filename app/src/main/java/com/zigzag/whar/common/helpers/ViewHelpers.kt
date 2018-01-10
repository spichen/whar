package com.zigzag.whar.common.helpers

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.zigzag.whar.R
import org.jetbrains.anko.design.longSnackbar

/**
 * Created by salah on 6/1/18.
 */
object ViewHelpers {
    fun whiteLongSnackBar(view : View, message:String) : View
    {
        val snackbarView = longSnackbar(view,message).view
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.context,android.R.color.white))
        snackbarView.findViewById<TextView>(android.support.design.R.id.snackbar_text).setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimaryDark))
        return snackbarView
    }
}